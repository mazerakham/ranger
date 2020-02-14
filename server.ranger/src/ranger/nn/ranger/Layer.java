package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;
import static ox.util.Functions.index;
import static ox.util.Functions.map;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Maps;

import ox.Json;
import ranger.math.Vector;
import ranger.nn.ranger.Neuron.NeuronType;

public class Layer {

  // With this probability, a new dendrite will be grown to a predecessor by a newly-created neuron.
  private static final double DENDRITE_SPARSITY_CONSTANT = 0.3;

  /**
   * Special map for the input layer to map its inputs to its input neurons.
   */
  private Map<Integer, UUID> inputMap = null;

  /**
   * Special map for the output layer to map output labels to output neurons.
   */
  private Map<Integer, UUID> outputMap = null;

  /**
   * The neurons.
   */
  protected Map<UUID, Neuron> neurons = Maps.newHashMap();

  /**
   * Forward prop stimuli & activations.
   */
  private SignalVector dendriteStimulus;
  private Map<UUID, Double> preAxonActivation;
  private SignalVector axonActivation;

  /**
   * Backprop signals.
   */
  private Map<UUID, Double> axonSignal;
  private Map<UUID, Double> preAxonSignal;
  private Map<UUID, Double> dendriteSignal;

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
    for (int i = 0; i < neurons.size(); i++) {
      neurons.get(i).loadDendriteStimulus(dendriteStimulus); // The neuron will use its mask and signal strength.
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
    axonSignal = Maps.newHashMap();
    for (int i = 0; i < signal.size(); i++) {
      UUID uuid = this.outputMap.get(i);
      axonSignal.put(uuid, signal.getEntry(i));
      neurons.get(uuid).loadAxonSignal(signal.getEntry(i));
    }
    return this;
  }
  
  public Layer loadAxonSignal(Map<UUID, Double> signal) {
    this.axonSignal = signal;
    for (Neuron neuron : neurons.values()) {
      neuron.loadAxonSignal(signal.get(neuron.uuid));
    }
    return this;
  }

  public Layer computeDendriteSignal(Layer prev) {
    Map<UUID, Double> totalSignal = Maps.newHashMap();
    for (Neuron neuron : neurons.values()) {
      Map<UUID, Double> neuronSignal = neuron.computeDendriteSignal();
      for (Entry<UUID, Double> entry : neuronSignal.entrySet()) {
        totalSignal.merge(entry.getKey(), entry.getValue(), (s1, s2) -> s1 + s2);
      }
    }
    this.dendriteSignal = totalSignal;
    return this;
  }

  public Map<UUID, Double> getDendriteSignal() {
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
    ret.outputMap = Maps.newHashMap();
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
  public void updateNeurons(Layer prev, Layer next) {
    for (Neuron neuron : neurons.values()) {
      neuron.update();
      if (neuron.isDead()) {
        neurons.remove(neuron.uuid);
        next.removeDendritesTo(neuron.uuid);
      }
    }
  }

  private void removeDendritesTo(UUID neuronUUID) {
    for (Neuron neuron : neurons.values()) {
      neuron.removeDendritesTo(neuronUUID);
    }
  }

  public boolean isOnlyIdentity() {
    for (int i = 0; i < neurons.size(); i++) {
      if (neurons.get(i).type != NeuronType.IDENTITY) {
        return false;
      }
    }
    return true;
  }

  public int size() {
    return neurons.size();
  }

  public static Layer fromJson(Json json) {
    Layer ret = new Layer();
    ret.neurons = index(map(json.getJson("neurons").asJsonArray(), Neuron::fromJson), neuron -> neuron.uuid);
    if (json.hasKey("inputMap")) {

    }
    return ret;
  }

  public Json toJson() {
    return Json.object()
        .with("neurons", Json.array(neurons.values(), Neuron::toJson));
  }

}
