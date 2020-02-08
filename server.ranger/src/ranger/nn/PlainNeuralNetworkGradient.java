package ranger.nn;

import java.util.List;

import com.google.common.collect.Lists;

import ranger.arch.PlainNeuralNetworkSpecs;
import ranger.math.Matrix;
import ranger.math.Vector;

public class PlainNeuralNetworkGradient {

  public final int numLayers;

  public List<Matrix> dW;

  public List<Vector> db;

  public PlainNeuralNetworkGradient(PlainNeuralNetworkSpecs specs) {
    this.numLayers = specs.numLayers;
    this.dW = Lists.newArrayListWithCapacity(specs.numLayers - 1);
    this.db = Lists.newArrayListWithCapacity(specs.numLayers - 1);
    for (int i = 0; i < specs.numLayers - 1; i++) {
      dW.add(Matrix.zeros(specs.layerSizes.get(i + 1), specs.layerSizes.get(i)));
      db.add(Vector.zeros(specs.layerSizes.get(i + 1)));
    }
  }

  /**
   * HACHHACK MUTATES this gradient.
   */
  public void add(PlainNeuralNetworkGradient that) {
    for (int i = 0; i < this.numLayers - 1; i++) {
      this.dW.set(i, this.dW.get(i).plus(that.dW.get(i)));
      this.db.set(i, this.db.get(i).plus(that.db.get(i)));
    }
  }

  /**
   * HACKHACK Mutates this gradient.
   */
  public void scale(double d) {
    for (int i = 0; i < this.numLayers - 1; i++) {
      this.dW.set(i, this.dW.get(i).scale(d));
      this.db.set(i, this.db.get(i).scale(d));
    }
  }
}
