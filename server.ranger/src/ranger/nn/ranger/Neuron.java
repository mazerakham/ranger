package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import ox.Json;

public class Neuron {

  /**
   * Starting state for new hidden neurons.
   */
  public static final double SIGMA_0 = 0.25;

  /**
   * The state-value threshold past which a hidden neuron starts to have signal strength.
   */
  public static final double SIGMA_1 = 0.2;

  /**
   * The state-value threshold at which a hidden neuron's signal strength saturates to 1.
   */
  public static final double SIGMA_2 = 0.4;

  /**
   * How much the utility of this neuron is taken into account for determining the delta-s.
   */
  public static final double UTILITY_COEFFICIENT = 0.01;

  /**
   * How much this neuron pays for each dendrite at *every* step.
   */
  public static final double DENDRITE_PENALTY = 0.02;

  /**
   * Dendrite Growth Rate
   */
  public static final double DENDRITE_GROWTH_RATE = 0.3;

  /**
   * INPUT, HIDDEN, IDENTITY, OUTPUT have different behaviors.
   */
  public NeuronType type;

  /**
   * Used by dendrites to identity the neurons they're listening to.
   */
  public UUID uuid;

  public Dendrites dendrites;
  public double bias;
  public ActivationFunction activationFunction;
  public double s; // The State-Value - represents the "health" of the neuron.

  // Forward prop.
  private SignalVector dendriteStimulus; // Stimulus at the dendrites, filtered by this neuron's own signal strength.
  private Double preActivation;
  private Signal activation; // A pair consisting of a value and signal strength.

  // Backward prop.
  private Double axonSignal; // Stimulus at the axon.
  private Double preAxonSignal;
  private Map<UUID, Double> dendriteSignal; // Back-prop signal, scaled down by this neuron's signal strength.

  public static Neuron inputNeuron() {
    Neuron ret = new Neuron();
    ret.type = NeuronType.INPUT;
    ret.s = 1;
    ret.uuid = UUID.randomUUID();
    return ret;
  }

  public static Neuron identityNeuron(Neuron neuron) {
    Neuron ret = new Neuron();
    ret.uuid = neuron.uuid;
    ret.type = NeuronType.IDENTITY;
    ret.dendrites = new Dendrites().add(neuron.uuid, 1.0);
    ret.bias = 0;
    ret.activationFunction = ActivationFunction.IDENTITY;
    ret.s = neuron.s;
    return ret;
  }

  public static Neuron hiddenNeuron() {
    Neuron ret = new Neuron();
    ret.type = NeuronType.HIDDEN;
    ret.activationFunction = ActivationFunction.RELU;
    ret.s = SIGMA_0;
    ret.uuid = UUID.randomUUID();
    return ret;
  }

  public static Neuron outputNeuron(int inSize) {
    Neuron ret = new Neuron();
    ret.type = NeuronType.OUTPUT;
    ret.bias = 0;
    ret.activationFunction = ActivationFunction.IDENTITY;
    ret.uuid = UUID.randomUUID();
    ret.s = 1;
    return ret;
  }

  public void maybeGrowDendritesTo(UUID predecessorUUID, Random random) {
    double proba;
    if (this.type == NeuronType.OUTPUT) {
      proba = 1.0;
    } else if (this.type == NeuronType.IDENTITY) {
      proba = 0.0;
    } else {
      proba = this.type == NeuronType.OUTPUT ? 1.0 : DENDRITE_GROWTH_RATE / this.dendrites.size();
    }

    if (random.nextDouble() < proba) {
      this.dendrites.add(predecessorUUID, 0.0);
    }
  }

  public void loadInputActivation(double d) {
    checkState(this.type == NeuronType.INPUT);
    this.activation = new Signal(d, 1.0);
  }

  public void loadDendriteStimulus(SignalVector stimulus) {
    this.dendriteStimulus = stimulus;
  }

  public Signal computeAxonActivation() {
    preActivation = dendrites.computeAxonPreactivation(this.dendriteStimulus, this.getSignalStrength()) + bias;
    activation = new Signal(this.activationFunction.apply(preActivation), getSignalStrength());
    return activation;
  }

  public void loadAxonSignal(double signal) {
    this.axonSignal = signal;
  }

  /**
   * Backprop signal, returned as map to identify which neurons get which signals. For now, we backprop scalars rather
   * than full-fledged Signals.
   */
  public Map<UUID, Double> computeDendriteSignal() {
    preAxonSignal = this.activationFunction.derivativeAt(this.preActivation);
    return dendriteSignal = dendrites.computeDendriteSignal(preAxonSignal, getSignalStrength());
  }

  /**
   * Update algorithm as described in:
   * 
   * https://docs.google.com/document/d/1aoc5v1AYzr4vm1qP1HU_QrW-s04_xY-pVQVDx8XpXCg/edit#heading=h.bc6io8jn10hw
   */
  public void update() {
    double utility = activation.value * axonSignal;
    s = Math.min(s + UTILITY_COEFFICIENT * utility - DENDRITE_PENALTY * (1 + dendrites.size()), 1.0);
    dendrites.update(dendriteSignal, getLearningRate());
    double db = this.preAxonSignal;
    bias = bias - db * getLearningRate();
  }

  public void removeDendritesTo(UUID uuid) {
    this.dendrites.remove(uuid);
  }

  public boolean isDead() {
    return s <= 0.0;
  }

  private double getLearningRate() {
    return 0.05;
  }

  private double getSignalStrength() {
    if (s < SIGMA_1) {
      return 0.0;
    } else if (s < SIGMA_2) {
      return (s - SIGMA_1) / (SIGMA_2 - SIGMA_1);
    } else {
      return 1;
    }
  }

  public static Neuron fromJson(Json json) {
    Neuron ret = new Neuron();
    ret.type = json.getEnum("type", NeuronType.class);
    ret.dendrites = Dendrites.fromJson(json.getJson("dendrites"));
    ret.bias = json.getDouble("bias");
    ret.s = json.getDouble("s");
    ret.activationFunction = ActivationFunction.fromJson(json.get("activationFunction"));
    ret.uuid = UUID.fromString(json.get("uuid"));
    return ret;
  }

  public Json toJson() {
    return Json.object()
        .with("uuid", uuid.toString())
        .with("type", type)
        .with("dendrites", dendrites == null ? null : dendrites.toJson())
        .with("bias", bias)
        .with("s", s)
        .with("activationFunction", activationFunction == null ? null : activationFunction.toJson());
  }

  public static enum NeuronType {
    INPUT, OUTPUT, HIDDEN, IDENTITY
  }

}
