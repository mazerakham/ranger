package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;

import java.util.UUID;

import ox.Json;
import ranger.math.SparseVector;

public class Neuron {

  /**
   * INPUT, HIDDEN, IDENTITY, OUTPUT have different behaviors.
   */
  public NeuronType type;

  /**
   * Used by dendrites to identity the neurons they're listening to.
   */
  public UUID uuid;

  public SparseVector dendrites;
  public double bias;
  public ActivationFunction activationFunction;

  // Forward prop.
  private SparseVector dendriteActivation; // Stimulus at the dendrites
  private Double preAxonActivation;
  private Double axonActivation; // A pair consisting of a value and signal strength.

  // Backward prop.
  private Double axonSignal; // Backprop signal received at the axon from successors.
  private Double preAxonSignal;
  private SparseVector dendriteSignal; // Back-prop signal to predecessors.

  public static Neuron inputNeuron() {
    Neuron ret = new Neuron();
    ret.type = NeuronType.INPUT;
    ret.uuid = UUID.randomUUID();
    return ret;
  }

  public static Neuron identityNeuron(Neuron neuron) {
    Neuron ret = new Neuron();
    ret.uuid = UUID.randomUUID();
    ret.type = NeuronType.IDENTITY;
    ret.dendrites = new SparseVector();
    ret.dendrites.put(neuron.uuid, 1.0);
    ret.bias = 0;
    ret.activationFunction = ActivationFunction.IDENTITY;
    return ret;
  }

  public static Neuron hiddenNeuron() {
    Neuron ret = new Neuron();
    ret.type = NeuronType.HIDDEN;
    ret.activationFunction = ActivationFunction.RELU;
    ret.uuid = UUID.randomUUID();
    return ret;
  }

  public static Neuron outputNeuron(int inSize) {
    Neuron ret = new Neuron();
    ret.type = NeuronType.OUTPUT;
    ret.bias = 0;
    ret.activationFunction = ActivationFunction.IDENTITY;
    ret.uuid = UUID.randomUUID();
    return ret;
  }

  public void loadInputActivation(double d) {
    checkState(this.type == NeuronType.INPUT);
    this.axonActivation = d;
  }

  public void loadDendriteActivation(SparseVector dendriteActivation) {
    this.dendriteActivation = dendriteActivation;
  }

  public double computeAxonActivation() {
    preAxonActivation = this.dendrites.dot(this.dendriteActivation) + bias;
    axonActivation = this.activationFunction.apply(preAxonActivation);
    return axonActivation;
  }

  public void loadAxonSignal(double signal) {
    this.axonSignal = signal;
  }

  /**
   * Backprop signal, returned as map to identify which neurons get which signals. For now, we backprop scalars rather
   * than full-fledged Signals.
   */
  public SparseVector computeDendriteSignal() {
    preAxonSignal = axonSignal * activationFunction.derivativeAt(preAxonActivation);
    return dendriteSignal = dendrites.scale(preAxonSignal);
  }

  /**
   * Update algorithm as described in:
   * 
   * https://docs.google.com/document/d/1aoc5v1AYzr4vm1qP1HU_QrW-s04_xY-pVQVDx8XpXCg/edit#heading=h.bc6io8jn10hw
   */
  public void update() {
    throw new UnsupportedOperationException();
  }

  public void removeDendritesTo(UUID uuid) {
    this.dendrites.remove(uuid);
  }

  public void iHaveNoSignal() {
    // TODO: Should neurons do anything if they have no incoming dendrites? Maybe die?
    throw new UnsupportedOperationException();
  }

  public static Neuron fromJson(Json json) {
    Neuron ret = new Neuron();
    ret.type = json.getEnum("type", NeuronType.class);
    ret.dendrites = SparseVector.fromJson(json.getJson("dendrites"));
    ret.bias = json.getDouble("bias");
    ret.activationFunction = ActivationFunction.fromJson(json.get("activationFunction"));
    ret.uuid = UUID.fromString(json.get("uuid"));
    return ret;
  }

  public Json toJson() {
    Json ret = Json.object()
        .with("uuid", uuid.toString())
        .with("type", type)
        .with("dendrites", dendrites == null ? null : dendrites.toJson())
        .with("bias", bias)
        .with("activationFunction", activationFunction == null ? null : activationFunction.toJson())
        .with("activity", Json.object()
            .with("dendriteStimulus", dendriteActivation == null ? null : dendriteActivation.toJson())
            .with("preActivation", preAxonActivation)
            .with("activation", axonActivation)
            .with("axonSignal", axonSignal)
            .with("preAxonSignal", preAxonSignal)
            .with("dendriteSignal", dendriteSignal == null ? null : dendriteSignal.toJson()));
        
    return ret;
  }

  public static enum NeuronType {
    INPUT, OUTPUT, HIDDEN, IDENTITY
  }

}
