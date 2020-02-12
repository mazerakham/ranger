package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;
import java.util.Random;

import ox.Json;
import ranger.math.Matrix;
import ranger.math.Vector;

public class Layer {

  private int size;

  private List<Neuron> neurons;

  /**
   * These fields represent the temporary 'activation' state of the layer. They are undefined when the network is not
   * being used for forward/backpropagation.
   */
  private boolean isActive = false;

  /**
   * Forward prop stimuli & activations.
   */
  private Vector dendriteStimulus;
  private Vector preAxonActivation;
  private Vector axonActivation;

  /**
   * Backprop signals.
   */
  private Vector axonSignal;
  private Vector preAxonSignal;
  private Vector dendriteSignal;

  private ActivationFunction activationFunction = new ActivationFunction();
  private Vector bias;
  private Matrix incomingWeights;

  public Layer(int size) {
    this.size = size;
  }


  public Layer xavierInitialize(int incomingSize, Random random) {
    double stdDev = Math.sqrt(2.0 / incomingSize);
    this.incomingWeights = Matrix.fromFunction(size, incomingSize, (i, j) -> random.nextGaussian() * stdDev);
    this.bias = Vector.zeros(size);
    return this;
  }

  public Layer loadActivation(Vector activation) {
    this.isActive = true;
    this.dendriteStimulus = activation;
    this.preAxonActivation = activation;
    this.axonActivation = activation;
    return this;
  }

  public Vector getAxonActivation() {
    return this.axonActivation;
  }

  public Layer loadDendriteStimulus(Vector activation) {
    this.dendriteStimulus = activation;
    this.preAxonActivation = incomingWeights.multiply(dendriteStimulus).plus(bias);
    return this;
  }

  public void computeAxonActivation() {
    checkState(dendriteStimulus != null, "Cannot compute activation without stimulus.");
    preAxonActivation = incomingWeights.multiply(dendriteStimulus);
    axonActivation = activationFunction.apply(preAxonActivation);
  }

  public void clearActivations() {
    this.dendriteStimulus = null;
    this.preAxonActivation = null;
    this.axonActivation = null;
  }

  public Layer loadAxonSignal(Vector signal) {
    this.axonSignal = signal;
    return this;
  }

  public Layer computeDendriteSignal() {
    throw new UnsupportedOperationException();
  }

  public Vector getDendriteSignal() {
    return this.dendriteSignal;
  }

  /**
   * Construct a new layer consisting of identity neurons back to the given layer.
   */
  public static Layer identityLayer(Layer layer) {
    throw new UnsupportedOperationException();
  }

  /**
   * Link my dendrites back to an identity copy of the layer I used to be linked back to.
   */
  public void linkBackToIdentity(Layer layer) {
    throw new UnsupportedOperationException();
  }

  /**
   * Update state, dendrites, bias for each neuron. Kill them off if they meet the criteria for death.
   */
  public void updateNeurons() {
    throw new UnsupportedOperationException();
  }

  public boolean isOnlyIdentity() {
    for (int i = 0; i < neurons.size(); i++) {
      if (!neurons.get(i).isIdentity) {
        return false;
      }
    }
    return true;
  }

  public static Layer fromJson(Json json) {
    Layer ret = new Layer(json.getInt("size"));
    ret.incomingWeights = Matrix.fromJson(json.getJson("incomingWeights"));
    ret.bias = Vector.fromJson(json.getJson("bias"));
    ret.activationFunction = ActivationFunction.fromJson(json.get("activationFunction"));

    Json activity = json.getJson("activity");
    if (activity != null) {
      ret.dendriteStimulus = Vector.fromJson(activity.getJson("stimulus"));
      ret.preAxonActivation = Vector.fromJson(activity.getJson("preActivation"));
      ret.axonActivation = Vector.fromJson(activity.getJson("activation"));
    }

    return ret;
  }

  public Json toJson() {
    Json ret = Json.object()
        .with("size", size)
        .with("incomingWeights", incomingWeights != null ? incomingWeights.toJson() : null)
        .with("bias", bias != null ? bias.toJson() : null)
        .with("activationFunction", activationFunction != null ? activationFunction.toJson() : null);
    if (isActive) {
      ret.with("activity", Json.object()
          .with("stimulus", dendriteStimulus.toJson())
          .with("preActivation", preAxonActivation.toJson())
          .with("activation", axonActivation.toJson()));
    }
    return ret;
  }

}
