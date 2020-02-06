package ranger.nn;

import java.util.List;

import com.google.common.collect.Lists;

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

  public PlainNeuralNetwork initialize() {
    throw new UnsupportedOperationException();
  }

}
