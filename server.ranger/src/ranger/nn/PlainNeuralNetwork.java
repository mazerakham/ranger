package ranger.nn;

import static ox.util.Functions.map;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import ranger.math.RangerMath;

import ox.Json;
import ranger.arch.PlainNeuralNetworkSpecs;
import ranger.math.Matrix;
import ranger.math.Vector;

public class PlainNeuralNetwork {

  private static final double L1_REG_CONSTANT = 0.01;
  
  /**
   * Includes input layer and output layer.
   */
  public final PlainNeuralNetworkSpecs specs;

  /**
   * Connection weights. W[i] is weights from layer i to layer i+1. Thus i belongs to [0, N-2].
   */
  private final List<Matrix> W;

  /**
   * Biases. b[i] is bias for layer i+1. Thus i belongs to [0, N-2].
   */
  private final List<Vector> b;

  public PlainNeuralNetwork(PlainNeuralNetworkSpecs specs) {
    this.specs = specs;
    this.W = Lists.newArrayListWithCapacity(specs.numLayers - 1);
    this.b = Lists.newArrayListWithCapacity(specs.numLayers - 1);
  }

  public PlainNeuralNetwork(PlainNeuralNetworkSpecs specs, List<Matrix> W, List<Vector> b) {
    this.specs = specs;
    this.W = W;
    this.b = b;
  }

  /**
   * Xavier Initialization.
   */
  public PlainNeuralNetwork initialize(Random random) {
    for (int i = 0; i < specs.numLayers - 1; i++) {
      int fanIn = specs.layerSizes.get(i);
      int fanOut = specs.layerSizes.get(i + 1);
      W.add(Matrix.fromFunction(fanOut, fanIn, (ii, j) -> random.nextGaussian() * Math.sqrt(2.0 / fanIn)));
      b.add(Vector.zeros(fanOut));
    }
    return this;
  }

  /**
   * Feed-forward with relu activations in each hidden layer, identity activation on output layer.
   * 
   * x_i+1 = Relu(W_i x_i + b_i+1)
   */
  public Vector estimate(Vector in) {
    int N = specs.numLayers;
    Vector activation = in; // activation 0.
    for (int i = 0; i < N - 2; i++) {
      // Compute activation i+1 given activation i.
      activation = RangerMath.relu(W.get(i).multiply(activation).plus(b.get(i)));
    }
    // Compute activation N-1, a.k.a. the output. Identity activation function.
    return W.get(N - 2).multiply(activation).plus(b.get(N - 2));
  }

  public PlainNeuralNetworkGradient lossGradient(Vector datapoint, Vector label) {
    PlainActivation activation = computeActivation(datapoint);
    return lossGradient(activation, label);
  }

  public void update(PlainNeuralNetworkGradient gradient, double learningRate) {
    for (int i = 0; i < this.specs.numLayers - 1; i++) {
      this.W.set(i, this.W.get(i).plus(gradient.dW.get(i).scale(-learningRate)));
      this.b.set(i, this.b.get(i).plus(gradient.db.get(i).scale(-learningRate)));
    }
  }

  private PlainActivation computeActivation(Vector in) {
    PlainActivation ret = new PlainActivation();
    ret.addPreActivation(in);
    Vector lastActivation = in;
    for (int i = 0; i < this.specs.numLayers - 1; i++) {
      // Given last activation i, compute preActivation i+1 and activation i+1.
      Vector nextPreActivation = W.get(i).multiply(lastActivation).plus(b.get(i));
      ret.addPreActivation(nextPreActivation);
      lastActivation = RangerMath.relu(nextPreActivation);
    }
    return ret;
  }

  // Back-propagation!
  private PlainNeuralNetworkGradient lossGradient(PlainActivation activation, Vector label) {
    PlainNeuralNetworkGradient ret = new PlainNeuralNetworkGradient(this.specs);
    int N = this.specs.numLayers;
    Vector xGrad = activation.getPreActivation(N - 1).minus(label); // RMSE Gradient, or xGrad N - 1.
    for (int i = N - 2; i >= 0; i--) {
      // Given xGrad i + 1, compute dW i, db i, and xGrad i,
      ret.dW.set(i,
          xGrad.asColumnMatrix().multiply(RangerMath.relu(activation.getPreActivation(i)).asRowMatrix())
              .plus(RangerMath.signum(this.W.get(i)).scale(L1_REG_CONSTANT)));
      ret.db.set(i, xGrad);
      xGrad = this.W.get(i).transpose().multiply(xGrad).otimes(RangerMath.heaviside(activation.getPreActivation(i)));
    }
    return ret;
  }

  public static PlainNeuralNetwork fromJson(Json json) {
    return new PlainNeuralNetwork(
        PlainNeuralNetworkSpecs.fromJson(json.getJson("specs")),
        map(json.getJson("W").asJsonArray(), Matrix::fromJson),
        map(json.getJson("b").asJsonArray(), Vector::fromJson));
  }

  public Json toJson() {
    return Json.object()
        .with("specs", specs.toJson())
        .with("W", Json.array(W, Matrix::toJson))
        .with("b", Json.array(b, Vector::toJson));
  }

}
