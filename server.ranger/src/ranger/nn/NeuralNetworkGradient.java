package ranger.nn;

import ranger.math.Matrix;
import ranger.math.Vector;

public class NeuralNetworkGradient {

  public final Matrix grad_w1, grad_w2;
  public final Vector grad_b1, grad_b2;

  /**
   * Initializes a gradient at zero of a size compatible with the neural network's weights.
   */
  public NeuralNetworkGradient(SingleLayerNeuralNetwork nn) {
    grad_w1 = Matrix.zeros(nn.hlSize, nn.inSize);
    grad_b1 = Vector.zeros(nn.hlSize);
    grad_w2 = Matrix.zeros(nn.outSize, nn.hlSize);
    grad_b2 = Vector.zeros(nn.outSize);
  }

  public NeuralNetworkGradient(Matrix w1, Vector b1, Matrix w2, Vector b2) {
    this.grad_w1 = w1;
    this.grad_b1 = b1;
    this.grad_w2 = w2;
    this.grad_b2 = b2;
  }

  public NeuralNetworkGradient scale(double d) {
    return new NeuralNetworkGradient(grad_w1.scale(d), grad_b1.scale(d), grad_w2.scale(d), grad_b2.scale(d));
  }

  public NeuralNetworkGradient plus(NeuralNetworkGradient that) {
    return new NeuralNetworkGradient(this.grad_w1.plus(that.grad_w1), this.grad_b1.plus(that.grad_b1),
        this.grad_w2.plus(that.grad_w2), this.grad_b2.plus(that.grad_b2));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    return String.format("w1 size: %.2f\nb1 size: %.2f\nw2 size: %.2f\nb2 size: %.2f", grad_w1.frobeniusNorm(),
        grad_b1.magnitude(), grad_w2.frobeniusNorm(), grad_b2.magnitude());
  }
}
