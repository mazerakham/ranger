package ranger.nn;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class PlainNeuralNetwork {

  /**
   * Includes input layer and output layer.
   */
  private final int numLayers;

  private final ImmutableList<Integer> sizes;

  public PlainNeuralNetwork(int numLayers, List<Integer> sizes) {
    checkState(numLayers == sizes.size());
    this.numLayers = numLayers;
    this.sizes = ImmutableList.copyOf(sizes);
  }
}
