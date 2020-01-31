package ranger.nn;

import java.util.Random;

import ranger.data.Dataset;
import ranger.math.Matrix;
import ranger.math.RangerMath;
import ranger.math.Vector;

public class NeuralNetwork {

  private int inSize;
  private int hlSize;
  private int outSize;

  private Matrix inToHl;
  private Vector hlBias;

  private Matrix hlToOut;
  private Vector outBias;

  public NeuralNetwork(int in, int hl, int out) {
    this.inSize = in;
    this.hlSize = hl;
    this.outSize = out;
  }

  public NeuralNetwork randomlyInitialize(Random r) {
    double std1 = Math.sqrt(2.0 / (inSize + hlSize));
    double std2 = Math.sqrt(2.0 / (outSize + hlSize));
    inToHl = Matrix.initializeFromFunction(hlSize, inSize, (i, j) -> std1 * r.nextGaussian());
    hlToOut = Matrix.initializeFromFunction(outSize, hlSize, (i, j) -> std2 * r.nextGaussian());
    hlBias = Vector.zeros(hlSize);
    outBias = Vector.zeros(outSize);
    return this;
  }

  public NeuralNetwork fit(Dataset dataset) {
    new SGDTrainer(dataset, this).train();
    return this;
  }

  public NeuralNetworkGradient computeBatchGradient(Matrix datapoints, Matrix labels) {
    throw new UnsupportedOperationException();
  }

  public Vector estimate(Vector in) {
    return hlToOut.multiply(RangerMath.relu(inToHl.multiply(in).plus(hlBias))).plus(outBias);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Size: (%d, %d, %d)\n", inSize, hlSize, outSize));
    sb.append("First matrix:\n");
    sb.append(inToHl.toString() + "\n");
    sb.append("Second matrix:\n");
    sb.append(hlToOut.toString());
    return sb.toString();
  }
}
