package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;
import static ox.util.Functions.map;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import ox.Json;
import ranger.math.Vector;

public class RangerNetwork implements Function<Vector, Vector> {

  /**
   * When this number is higher, new neurons will have a greater tendency to go to layers that already have more
   * neurons---hence slowing the growth of new layers.
   */
  public static final double LAYER_SIZE_GROWTH_EXPONENT = 1.2;

  /**
   * Once a layer reaches 6 neurons, its growth *rate* no longer increases.
   */
  public static final double LAYER_GROWTH_CAP = 6;

  private final int inSize;
  private final int outSize;

  private Layer inputLayer;
  private List<Layer> layers = Lists.newArrayList();
  private Layer outputLayer;

  NeuronGenerator neuronGenerator = new NeuronGenerator();

  public RangerNetwork(int inSize, int outSize) {
    this.inSize = inSize;
    this.outSize = outSize;
  }

  /**
   * Call immediately after the constructor to initialize as a (He/Xavier) randomly-weighted linear regressor from input
   * layer to output layer.
   */
  public RangerNetwork initialize(Random random) {
    inputLayer = Layer.inputLayer(inSize);
    outputLayer = Layer.outputLayer(inSize, outSize, inputLayer);
    throw new UnsupportedOperationException();
  }

  /**
   * Forward propagate the input through the network, computing pre-activations and activations throughout.
   */
  public void propagateForward(Vector input) {
    inputLayer.loadInputAxonActivation(input);
    for (int i = 1; i < layers.size(); i++) {
      layers.get(i).loadDendriteActivation(layers.get(i - 1).getAxonActivation()).computeAxonActivation();
    }
  }

  /**
   * Backward propagate the label through the network, computing error gradients of every kind.
   */
  public void propagateBackward(Vector label) {
    int L = layers.size();
    outputLayer.loadOutputAxonSignal(outputLayer.getAxonActivationAsOutput().minus(label))
        .computeDendriteSignal();
    for (int i = L - 2; i >= 1; i--) {
      layers.get(i).loadAxonSignal(layers.get(i + 1).getDendriteSignal())
          .computeDendriteSignal();
    }
  }

  /**
   * For this non-recurrent ranger network, all activations must be cleared before a new computation can take place.
   */
  public void clearActivations() {
    layers.forEach(Layer::clearActivations);
  }

  /**
   * Gets the output of this neural network, given the input. 
   */
  public Vector estimate(Vector v) {
    propagateForward(v);
    return outputLayer.getAxonActivationAsOutput();
  }

  public static RangerNetwork fromJson(Json json) {
    checkState(json.hasKey("inSize"), "Ranger network couldn't parse JSON without inSize");
    checkState(json.hasKey("outSize"), "Ranger network couldn't parse JSON without outSize");
    checkState(json.hasKey("layers"), "Ranger network couldn't parse JSON without layers field.");
    RangerNetwork ret = new RangerNetwork(json.getInt("inSize"), json.getInt("outSize"));
    ret.layers = map(json.getJson("layers").asJsonArray(), Layer::fromJson);
    ret.inputLayer = ret.layers.get(0);
    ret.outputLayer = ret.layers.get(ret.layers.size() - 1);
    return ret;
  }

  public Json toJson() {
    return Json.object()
        .with("inSize", inSize)
        .with("outSize", outSize)
        .with("layers", map(layers, Layer::toJson));
  }

  @Override
  public Vector apply(Vector v) {
    return estimate(v);
  }
}
