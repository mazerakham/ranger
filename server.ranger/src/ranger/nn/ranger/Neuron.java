package ranger.nn.ranger;

import java.util.Random;

import com.google.common.collect.Lists;

import ox.Json;
import ox.Log;
import ranger.math.Vector;

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

  public NeuronType type;
  public Vector dendrites;
  public DendriteMask dendriteMask;
  public double bias;
  public ActivationFunction activationFunction;
  public double s; // The State-Value - represents the "health" of the neuron.

  // Forward prop.
  private Vector dendriteStimulus; // Stimulus at the dendrites, filtered by this neuron's own signal strength.
  private Double preActivation;
  private Signal activation; // A pair consisting of a value and signal strength.

  // Backward prop.
  private Double axonSignal; // Stimulus at the axon.
  private Double preAxonSignal;
  private Vector dendriteSignal; // Back-prop signal, scaled down by this neuron's signal strength.

  public static Neuron inputNeuron() {
    Neuron ret = new Neuron();
    ret.type = NeuronType.INPUT;
    ret.s = 1;
    return ret;
  }

  public static Neuron identityNeuron(int i, double s) {
    Neuron ret = new Neuron();
    ret.type = NeuronType.IDENTITY;
    ret.dendrites = new Vector(1.0);
    ret.bias = 0;
    ret.activationFunction = ActivationFunction.IDENTITY;
    ret.dendriteMask = new DendriteMask(Lists.newArrayList(i));
    ret.s = s;
    return ret;
  }

  public static Neuron hiddenNeuron() {
    Neuron ret = new Neuron();
    ret.type = NeuronType.HIDDEN;
    ret.activationFunction = ActivationFunction.RELU;
    ret.s = SIGMA_0;
    return ret;
  }

  public static Neuron outputNeuron(int inSize) {
    Neuron ret = new Neuron();
    double stdDev = Math.sqrt(2.0 / inSize);
    ret.type = NeuronType.OUTPUT;
    ret.bias = 0;
    ret.activationFunction = ActivationFunction.IDENTITY;
    ret.s = 1;
    return ret;
  }

  public void initializeDendrites(DendriteMask mask, Random random) {
    if (mask.size() == 0) {
      this.dendriteMask = mask;
      this.dendrites = new Vector();
      return;
    }
    double stdDev = Math.sqrt(2 / mask.size());
    this.dendrites = Vector.fromFunction(mask.size(), i -> random.nextGaussian() * stdDev);
    this.dendriteMask = mask;
  }

  public void maybeGrowDendritesTo(int predecessorIndex, Random random) {
    double proba;
    if (this.type == NeuronType.OUTPUT) {
      proba = 1.0;
    } else if (this.type == NeuronType.IDENTITY) {
      proba = 0.0;
    } else {
      proba = this.type == NeuronType.OUTPUT ? 1.0 : DENDRITE_GROWTH_RATE / this.dendriteMask.size();
    }

    if (random.nextDouble() < proba) {
      this.dendriteMask.add(predecessorIndex);
      this.dendrites.addEntry(0.0);
    }
  }

  public void loadDendriteStimulus(SignalVector stimulus) {
    this.dendriteStimulus = getStimulusFromSignal(dendriteMask.mask(stimulus));
  }

  public Signal computeAxonActivation() {
    preActivation = dendrites.dot(dendriteStimulus) + bias;
    activation = new Signal(this.activationFunction.apply(preActivation), getSignalStrength());
    return activation;
  }

  public void loadAxonSignal(double signal) {
    this.axonSignal = signal;
  }

  public Vector computeDendriteSignal() {
    preAxonSignal = this.activationFunction.derivativeAt(this.preActivation);
    return dendriteSignal = Vector.fromFunction(dendriteMask.size(), i -> preAxonSignal * dendrites.getEntry(i))
        .scale(getSignalStrength());
  }

  /**
   * Update algorithm as described in:
   * 
   * https://docs.google.com/document/d/1aoc5v1AYzr4vm1qP1HU_QrW-s04_xY-pVQVDx8XpXCg/edit#heading=h.bc6io8jn10hw
   */
  public void update() {
    double utility = activation.value * axonSignal;
    s = Math.min(s + UTILITY_COEFFICIENT * utility - DENDRITE_PENALTY * (1 + dendriteMask.size()), 1.0);
    Vector dW = dendriteSignal.otimes(dendriteStimulus);
    dendrites = dendrites.minus(dW.scale(getLearningRate()));
    double db = this.preAxonSignal;
    bias = bias - db * getLearningRate();
  }

  public void remapDendrites(int neuronNumber) {
    Log.debug("Neuron with dendriteMask %s remapping dendrites for neuron number %d", this.dendriteMask.toJson(),
        neuronNumber);
    if (dendriteMask.contains(neuronNumber)) {
      int index = dendriteMask.getIndex(neuronNumber);
      this.dendrites.remove(index);
    }

    dendriteMask.removeAndRemap(neuronNumber);
    Log.debug("Dendrite mask after: %s", this.dendriteMask.toJson());
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

  // Algorithm described in:
  // https://docs.google.com/document/d/1aoc5v1AYzr4vm1qP1HU_QrW-s04_xY-pVQVDx8XpXCg/edit#heading=h.luoiw41gac4j
  private Vector getStimulusFromSignal(SignalVector signalVector) {
    return Vector.fromFunction(signalVector.size(), i -> getStimulusFromSignal(signalVector.signals.get(i)));
  }

  private double getStimulusFromSignal(Signal signal) {
    return signal.value * (1 - Math.max(this.getSignalStrength() - signal.strength, 0));
  }

  public static Neuron fromJson(Json json) {
    Neuron ret = new Neuron();
    ret.type = json.getEnum("type", NeuronType.class);
    ret.dendriteMask = DendriteMask.fromJson(json.getJson("dendriteMask"));
    ret.dendrites = Vector.fromJson(json.getJson("dendrites"));
    ret.bias = json.getDouble("bias");
    ret.s = json.getDouble("s");
    ret.activationFunction = ActivationFunction.fromJson(json.get("activationFunction"));
    return ret;
  }

  public Json toJson() {
    return Json.object()
        .with("type", type)
        .with("dendriteMask", dendriteMask == null ? null : dendriteMask.toJson())
        .with("dendrites", dendrites == null ? null : dendrites.toJson())
        .with("bias", bias)
        .with("s", s)
        .with("activationFunction", activationFunction == null ? null : activationFunction.toJson());
  }

  public static enum NeuronType {
    INPUT, OUTPUT, HIDDEN, IDENTITY
  }

}
