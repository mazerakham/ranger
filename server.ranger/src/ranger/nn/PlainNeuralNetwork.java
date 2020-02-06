package ranger.nn;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import ox.Json;
import ranger.arch.PlainNeuralNetworkSpecs;
import ranger.math.Matrix;
import ranger.math.Vector;

public class PlainNeuralNetwork {

  /**
   * Includes input layer and output layer.
   */
  public final PlainNeuralNetworkSpecs specs;

  private final List<Matrix> W;

  private final List<Vector> b;

  public PlainNeuralNetwork(PlainNeuralNetworkSpecs specs) {
    this.specs = specs;
    this.W = Lists.newArrayListWithCapacity(specs.numLayers - 1);
    this.b = Lists.newArrayListWithCapacity(specs.numLayers - 1);
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

  public Json toJson() {
    return Json.object()
        .with("specs", specs.toJson())
        .with("W", Json.array(W, Matrix::toJson))
        .with("b", Json.array(b, Vector::toJson));
  }

}
