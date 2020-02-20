package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import ox.Json;
import ranger.arch.JsonUtils;
import ranger.math.SparseVector;
import ranger.math.Vector;

public class Layer {

  // With this probability, a new dendrite will be grown to a predecessor by a newly-created neuron.
  private static final double INITIAL_DENDRITE_SPARSITY = 1.0;

  /**
   * Special map for the input layer to map its inputs to its input neurons.
   */
  private Map<Integer, UUID> inputMap = null;

  /**
   * Special map for the output layer to map output labels to output neurons.
   */
  private BiMap<Integer, UUID> outputMap = null;

  /**
   * The neurons.
   */
  protected Map<UUID, Neuron> neurons = Maps.newHashMap();

  /**
   * Forward prop activations.
   */
  private SparseVector dendriteActivation;
  private SparseVector preAxonActivation;
  private SparseVector axonActivation;

  /**
   * Backprop signals.
   */
  private SparseVector axonSignal;
  private SparseVector preAxonSignal;
  private SparseVector dendriteSignal;

  public Layer withNeuron(Neuron neuron) {
    neurons.put(neuron.uuid, neuron);
    return this;
  }

  public Layer loadInputVector(Vector inputVector) {
    throw new UnsupportedOperationException();
  }


  public Layer loadDendriteActivation(SparseVector dendriteActivation) {
    this.dendriteActivation = dendriteActivation;
    for (Neuron neuron : neurons.values()) {
      neuron.loadDendriteActivation(dendriteActivation);
    }
    return this;
  }

  public void computeAxonActivation() {
    checkState(dendriteActivation != null, "Cannot compute activation without stimulus.");
    axonActivation = new SparseVector();
    for (Neuron neuron : neurons.values()) {
      axonActivation.put(neuron.uuid, neuron.computeAxonActivation());
    }
  }

  public SparseVector getAxonActivation() {
    return this.axonActivation;
  }

  public Vector getAxonActivationAsOutput() {
    Map<UUID, Integer> orderMap = outputMap.inverse();
    Vector ret = Vector.zeros(orderMap.size());
    for (Entry<UUID, Double> entry : axonActivation.entrySet()) {
      ret.setEntry(orderMap.get(entry.getKey()), entry.getValue());
    }
    return ret;
  }

  public void clearActivations() {
    this.dendriteActivation = null;
    this.preAxonActivation = null;
    this.axonActivation = null;
  }

  public Layer loadInputAxonActivation(Vector signal) {
    axonActivation = new SparseVector();
    for (int i = 0; i < signal.size(); i++) {
      UUID uuid = this.inputMap.get(i);
      neurons.get(uuid).loadInputActivation(signal.getEntry(i));
      axonActivation.put(uuid, signal.getEntry(i));
    }
    return this;
  }

  public Layer loadOutputAxonSignal(Vector signal) {
    axonSignal = new SparseVector();
    for (int i = 0; i < signal.size(); i++) {
      UUID uuid = this.outputMap.get(i);
      axonSignal.put(uuid, signal.getEntry(i));
      neurons.get(uuid).loadAxonSignal(signal.getEntry(i));
    }
    return this;
  }
  
  public Layer loadAxonSignal(SparseVector signal) {
    this.axonSignal = signal;
    for (Neuron neuron : neurons.values()) {
      if (signal.containsKey(neuron.uuid)) {
        neuron.loadAxonSignal(signal.get(neuron.uuid));
      } else {
        neuron.iHaveNoSignal();
      }

    }
    return this;
  }

  public Layer computeDendriteSignal() {
    SparseVector totalSignal = new SparseVector();
    for (Neuron neuron : neurons.values()) {
      SparseVector neuronSignal = neuron.computeDendriteSignal();
      totalSignal = totalSignal.plus(neuronSignal);
    }
    this.dendriteSignal = totalSignal;
    return this;
  }

  public SparseVector getDendriteSignal() {
    return this.dendriteSignal;
  }

  public static Layer inputLayer(int inSize) {
    Layer ret = new Layer();
    ret.inputMap = Maps.newHashMap();
    for (int i = 0; i < inSize; i++) {
      Neuron neuron = Neuron.inputNeuron();
      ret.withNeuron(neuron);
      ret.inputMap.put(i, neuron.uuid);
    }
    return ret;
  }

  public static Layer outputLayer(int inSize, int outSize, Layer inputLayer) {
    Layer ret = new Layer();
    ret.outputMap = HashBiMap.create();
    for (int i = 0; i < outSize; i++) {
      Neuron neuron = Neuron.outputNeuron(inSize);
      ret.withNeuron(neuron);
      ret.outputMap.put(i, neuron.uuid);
    }

    return ret;
  }

  /**
   * Update state, dendrites, bias for each neuron. Kill them off if they meet the criteria for death.
   * 
   * 
   */
  public void updateNeurons(Layer next) {
    throw new UnsupportedOperationException();
  }

  public static Layer fromJson(Json json) {
    Layer ret = new Layer();
    Json neuronsJson = json.getJson("neurons");
    Map<UUID, Neuron> neurons = Maps.newHashMap();
    for (String uuid : neuronsJson.asJsonObject().keySet()) {
      neurons.put(UUID.fromString(uuid), Neuron.fromJson(neuronsJson.getJson(uuid)));
    }
    ret.neurons = neurons;
    ret.inputMap = JsonUtils.neuronIndexFromJson(json.getJson("inputMap"));
    ret.outputMap = JsonUtils.neuronIndexFromJson(json.getJson("outputMap"));
    return ret;
  }

  public Json toJson() {
    Json neuronsJson = Json.object();
    neurons.entrySet().forEach(entry -> neuronsJson.with(entry.getKey().toString(), entry.getValue().toJson()));
    return Json.object()
        .with("neurons", neuronsJson)
        .with("inputMap", JsonUtils.neuronIndexToJson(inputMap))
        .with("outputMap", JsonUtils.neuronIndexToJson(outputMap));
  }

}
