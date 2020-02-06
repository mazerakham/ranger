package ranger.nn;

import static ranger.math.RangerMath.heaviside;

import java.util.Random;

import ranger.math.RangerMath;

import ox.Json;
import ranger.math.Matrix;
import ranger.math.Vector;

/**
 * Represents a neural network (a,b,c) with input vectors of size a, a ReLU hidden layer of size b, and an output of
 * size c.
 */
public class SingleLayerNeuralNetwork {

  public int inSize;
  public int hlSize;
  public int outSize;

  private Matrix w1;
  private Vector b1; // ReLU hidden layer.

  private Matrix w2;
  private Vector b2; // output layer.

  public SingleLayerNeuralNetwork(int in, int hl, int out) {
    this.inSize = in;
    this.hlSize = hl;
    this.outSize = out;
  }

  /**
   * Xavier initialization.
   */
  public SingleLayerNeuralNetwork randomlyInitialize(Random r) {
    double std1 = Math.sqrt(2.0 / (inSize + hlSize));
    double std2 = Math.sqrt(2.0 / (outSize + hlSize));
    w1 = Matrix.fromFunction(hlSize, inSize, (i, j) -> std1 * r.nextGaussian());
    w2 = Matrix.fromFunction(outSize, hlSize, (i, j) -> std2 * r.nextGaussian());
    b1 = Vector.zeros(hlSize);
    b2 = Vector.zeros(outSize);
    return this;
  }

  /**
   * Computes the gradient (with respect to all the weights and biases) of the mean-squared-loss function for the given
   * batch of datapoints and labels.
   * 
   * @param datapoints a matrix of datapoints, each of which is a row.  
   * @param labels a matrix of the labels for those datapoints, each of which is a row. 
   */
  public NeuralNetworkGradient lossGradient(Vector datapoint, Vector label) {

    Vector h1 = w1.multiply(datapoint).plus(b1);
    Vector a1 = RangerMath.relu(h1);
    Vector out = w2.multiply(a1).plus(b2); // no activation function.

    Vector e = out.minus(label).scale(1.0/label.size());

    Vector grad_b2 = e;
    Matrix grad_w2 = e.asColumnMatrix().multiply(a1.asColumnMatrix().transpose());
    Vector grad_h1 = w2.transpose().multiply(e).otimes(heaviside(h1));
    Vector grad_b1 = grad_h1;
    Matrix grad_w1 = grad_h1.asColumnMatrix().multiply(datapoint.asColumnMatrix().transpose());
    
    return new NeuralNetworkGradient(grad_w1, grad_b1, grad_w2, grad_b2);
  }

  public void update(NeuralNetworkGradient gradient, double learningRate) {
    gradient = gradient.scale(-learningRate);
    w1 = w1.plus(gradient.grad_w1);
    b1 = b1.plus(gradient.grad_b1);
    w2 = w2.plus(gradient.grad_w2);
    b2 = b2.plus(gradient.grad_b2);
  }

  public Vector estimate(Vector in) {
    return w2.multiply(RangerMath.relu(w1.multiply(in).plus(b1))).plus(b2);
  }

  public Json toJson() {
    return Json.object()
        .with("size", Json.object()
            .with("inSize", inSize)
            .with("hlSize", hlSize)
            .with("outSize", outSize))
        .with("layers", Json.object()
            .with("w1", w1.toJson())
            .with("b1", b1.toJson())
            .with("w2", w2.toJson())
            .with("b2", b2.toJson()));
  }

  public static SingleLayerNeuralNetwork fromJson(Json json) {
    Json size = json.getJson("size");
    Json layers = json.getJson("layers");
    SingleLayerNeuralNetwork ret = new SingleLayerNeuralNetwork(size.getInt("inSize"), size.getInt("hlSize"), size.getInt("outSize"));
    ret.w1 = Matrix.fromJson(layers.getJson("w1"));
    ret.b1 = Vector.fromJson(layers.getJson("b1"));
    ret.w2 = Matrix.fromJson(layers.getJson("w2"));
    ret.b2 = Vector.fromJson(layers.getJson("b2"));
    return ret;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Size: (%d, %d, %d)\n", inSize, hlSize, outSize));
    sb.append("First matrix:\n");
    sb.append(w1.toString() + "\n");
    sb.append("Second matrix:\n");
    sb.append(w2.toString());
    return sb.toString();
  }

  @Override
  public SingleLayerNeuralNetwork clone() {
    SingleLayerNeuralNetwork ret = new SingleLayerNeuralNetwork(this.inSize, this.hlSize, this.outSize);
    ret.w1 = w1.clone();
    ret.b1 = b1.clone();
    ret.w2 = w2.clone();
    ret.b2 = b2.clone();
    return ret;
  }
}
