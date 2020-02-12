package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;
import static ox.util.Functions.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import ox.Json;
import ranger.math.Vector;

public class RangerNetwork implements Function<Vector, Vector> {

  private final int inSize;
  private final int outSize;

  private Layer inputLayer;
  private List<Layer> layers = Lists.newArrayList();
  private Layer outputLayer;

  public RangerNetwork(int inSize, int outSize) {
    this.inSize = inSize;
    this.outSize = outSize;
  }

  /**
   * Call immediately after the constructor to initialize as a (He/Xavier) randomly-weighted linear regressor from input
   * layer to output layer.
   */
  public RangerNetwork initialize(Random random) {
    inputLayer = new Layer(inSize);
    outputLayer = new Layer(outSize).xavierInitialize(inSize, random);
    layers.add(inputLayer);
    layers.add(outputLayer);
    return this;
  }

  /**
   * Insert an identity layer between each layer.
   */
  public void expand() {
    ArrayList<Layer> newLayers = Lists.newArrayList();
    for (int i = 0; i < layers.size() - 1; i++) {
      newLayers.add(layers.get(i));
      newLayers.add(Layer.identityLayer(layers.get(i)));
    }
    newLayers.add(outputLayer);
    this.layers = newLayers;
  }

  /**
   * Randomly insert neurons in the layers with random choice of dendrites.
   */
  public void addNewNeurons() {
    new NeuronGenerator(this).generateNeurons();
  }

  /**
   * Forward propagate the input through the network, computing pre-activations and activations throughout.
   */
  public void propagateForward(Vector input) {
    inputLayer.loadActivation(input);
    for (int i = 1; i < layers.size(); i++) {
      layers.get(i).loadDendriteStimulus(layers.get(i - 1).getAxonActivation()).computeAxonActivation();
    }
  }

  /**
   * Backward propagate the label through the network, computing error gradients of every kind.
   */
  public void propagateBackward(Vector label) {
    outputLayer.loadAxonSignal(label).computeDendriteSignal();
    for (int i = layers.size() - 2; i >= 0; i--) {
      layers.get(i).loadAxonSignal(layers.get(i + 1).getDendriteSignal()).computeDendriteSignal();
    }
  }

  /**
   * Update the internal state of each neuron. See
   */
  public void updateNeurons() {
    for (int i = 1; i < layers.size() - 1; i++) {
      layers.get(i).updateNeurons();
    }
  }

  /**
   * Remove any layers that consist only of identity neurons.
   */
  public void contract() {
    List<Layer> newLayers = new ArrayList<>();
    newLayers.add(inputLayer);
    for (int i = 1; i < layers.size() - 1; i++) {
      Layer current = layers.get(i);
      if (!current.isOnlyIdentity()) {
        newLayers.add(current);
      }
    }
    newLayers.add(outputLayer);
  }

  /**
   * For this non-recurrent ranger network, all activations must be cleared before a new computation can take place.
   */
  public void clearActivations() {
    layers.forEach(Layer::clearActivations);
  }

  public Vector estimate(Vector v) {
    propagateForward(v);
    return outputLayer.getAxonActivation();
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
