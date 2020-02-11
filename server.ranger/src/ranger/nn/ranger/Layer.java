package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;

import java.util.Random;

import ox.Json;
import ranger.math.Matrix;
import ranger.math.Vector;

public class Layer {

  private int size;

  /**
   * These fields represent the temporary 'activation' state of the layer. They are undefined when the network is not
   * being used for forward/backpropagation.
   */
  private boolean isActive = false;
  private Vector stimulus;
  private Vector preActivation;
  private Vector activation;

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
    this.stimulus = activation;
    this.preActivation = activation;
    this.activation = activation;
    return this;
  }

  public Vector getActivation() {
    return this.activation;
  }

  public Layer loadStimulus(Vector activation) {
    this.stimulus = activation;
    this.preActivation = incomingWeights.multiply(stimulus).plus(bias);
    return this;
  }

  public void computeActivation() {
    checkState(stimulus != null, "Cannot compute activation without stimulus.");
    preActivation = incomingWeights.multiply(stimulus);
    activation = activationFunction.apply(preActivation);
  }

  public void clearActivations() {
    this.stimulus = null;
    this.preActivation = null;
    this.activation = null;
  }

  public static Layer fromJson(Json json) {
    Layer ret = new Layer(json.getInt("size"));
    ret.incomingWeights = Matrix.fromJson(json.getJson("incomingWeights"));
    ret.bias = Vector.fromJson(json.getJson("bias"));
    ret.activationFunction = ActivationFunction.fromJson(json.get("activationFunction"));

    Json activity = json.getJson("activity");
    if (activity != null) {
      ret.stimulus = Vector.fromJson(activity.getJson("stimulus"));
      ret.preActivation = Vector.fromJson(activity.getJson("preActivation"));
      ret.activation = Vector.fromJson(activity.getJson("activation"));
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
          .with("stimulus", stimulus.toJson())
          .with("preActivation", preActivation.toJson())
          .with("activation", activation.toJson()));
    }
    return ret;
  }

}
