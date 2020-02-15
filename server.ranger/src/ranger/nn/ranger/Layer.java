package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import ox.Json;
import ox.Log;
import ranger.arch.JsonUtils;
import ranger.math.Vector;
import ranger.nn.ranger.Neuron.NeuronType;

public class Layer {

  // With this probability, a new dendrite will be grown to a predecessor by a newly-created neuron.
  private static final double DENDRITE_SPARSITY_CONSTANT = 1.0;

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
   * Forward prop stimuli & activations.
   */
  private SignalVector dendriteStimulus;
  private NeuronMap preAxonActivation;
  private SignalVector axonActivation;

  /**
   * Backprop signals.
   */
  private NeuronMap axonSignal;
  private NeuronMap preAxonSignal;
  private NeuronMap dendriteSignal;

  public void growNewNeuron(Layer prev, Layer next, Random random) {
    Neuron neuron = Neuron.hiddenNeuron();
    neurons.put(neuron.uuid, neuron);
    neuron.dendrites = Dendrites.randomDendrites(prev, DENDRITE_SPARSITY_CONSTANT, random);
    next.maybeGrowDendritesTo(neuron.uuid, random);
  }

  public void maybeGrowDendritesTo(UUID neuronUUID, Random random) {
    // Each neuron has a const / (# dendrites) probability of adding this neuron to their mask.
    for (Neuron neuron : neurons.values()) {
      neuron.maybeGrowDendritesTo(neuronUUID, random);
    }
  }

  public Layer withNeuron(Neuron neuron) {
    neurons.put(neuron.uuid, neuron);
    return this;
  }

  public Layer loadInputVector(Vector inputVector) {
    throw new UnsupportedOperationException();
    // This has to use the inputMapping or whatever. This should be renamed to loadInput, making clear it's only for the
    // input layer.
  }


  public Layer loadDendriteStimulus(SignalVector activation) {
    this.dendriteStimulus = activation;
    for (Neuron neuron : neurons.values()) {
      neuron.loadDendriteStimulus(dendriteStimulus);
    }
    return this;
  }

  public void computeAxonActivation() {
    checkState(dendriteStimulus != null, "Cannot compute activation without stimulus.");
    SignalVector axonActivation = new SignalVector();
    for (Neuron neuron : neurons.values()) {
      axonActivation.addSignal(neuron.uuid, neuron.computeAxonActivation());
    }
    this.axonActivation = axonActivation;
  }

  public SignalVector getAxonActivation() {
    return this.axonActivation;
  }

  public Vector getAxonActivationAsOutput() {
    SignalVector axonActivation = this.axonActivation;
    Map<UUID, Integer> orderMap = outputMap.inverse();
    Vector ret = Vector.zeros(orderMap.size());
    for (Entry<UUID, Signal> entry : axonActivation.signals.entrySet()) {
      ret.setEntry(orderMap.get(entry.getKey()), entry.getValue().value);
    }
    return ret;
  }

  public void clearActivations() {
    this.dendriteStimulus = null;
    this.preAxonActivation = null;
    this.axonActivation = null;
  }

  public Layer loadInputAxonActivation(Vector signal) {
    axonActivation = new SignalVector();
    for (int i = 0; i < signal.size(); i++) {
      UUID uuid = this.inputMap.get(i);
      neurons.get(uuid).loadInputActivation(signal.getEntry(i));
      axonActivation.addSignal(uuid, new Signal(signal.getEntry(i), 1.0));
    }
    return this;
  }

  public Layer loadOutputAxonSignal(Vector signal) {
    axonSignal = new NeuronMap();
    for (int i = 0; i < signal.size(); i++) {
      UUID uuid = this.outputMap.get(i);
      axonSignal.put(uuid, signal.getEntry(i));
      neurons.get(uuid).loadAxonSignal(signal.getEntry(i));
    }
    return this;
  }
  
  public Layer loadAxonSignal(NeuronMap signal) {
    this.axonSignal = signal;
    for (Neuron neuron : neurons.values()) {
      neuron.loadAxonSignal(signal.containsKey(neuron.uuid) ? signal.get(neuron.uuid) : 0.0);
    }
    return this;
  }

  public Layer computeDendriteSignal() {
    NeuronMap totalSignal = new NeuronMap();
    for (Neuron neuron : neurons.values()) {
      NeuronMap neuronSignal;
      try {
        neuronSignal = neuron.computeDendriteSignal();
      } catch (Exception e) {
        Log.debug("The layer that failed to compute dendrite signal was:");
        Log.debug(this.toJson().prettyPrint());
        throw e;
      }
      for (Entry<UUID, Double> entry : neuronSignal.entrySet()) {
        totalSignal.merge(entry.getKey(), entry.getValue(), (s1, s2) -> s1 + s2);
      }
    }
    this.dendriteSignal = totalSignal;
    return this;
  }

  public NeuronMap getDendriteSignal() {
    return this.dendriteSignal;
  }

  /**
   * Construct a new layer consisting of identity neurons back to the given layer.
   */
  public static Layer identityLayer(Layer layer) {
    Layer ret = new Layer();
    for (Neuron neuron : layer.neurons.values()) {
      ret.withNeuron(Neuron.identityNeuron(neuron));
    }
    return ret;
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
    List<UUID> markedForDeletion = new ArrayList<>();
    for (Neuron neuron : neurons.values()) {
      neuron.update();
      if (neuron.isDead()) {
        markedForDeletion.add(neuron.uuid);
      }
    }
    for (UUID uuid : markedForDeletion) {
      neurons.remove(uuid);
      next.removeDendritesTo(uuid);
    }
  }

  private void removeDendritesTo(UUID neuronUUID) {
    for (Neuron neuron : neurons.values()) {
      neuron.removeDendritesTo(neuronUUID);
    }
  }

  public boolean isOnlyIdentity() {
    for (Neuron neuron : neurons.values()) {
      if (neuron.type != NeuronType.IDENTITY) {
        return false;
      }
    }
    return true;
  }

  public int size() {
    return neurons.size();
  }

  public int hiddenSize() {
    int ret = 0;
    for (Neuron neuron : neurons.values()) {
      if (neuron.type == NeuronType.HIDDEN) {
        ret++;
      }
    }
    return ret;
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
