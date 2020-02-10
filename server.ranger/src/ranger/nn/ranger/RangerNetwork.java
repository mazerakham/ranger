package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;

import java.util.function.Function;

import ox.Json;
import ranger.math.Vector;

public class RangerNetwork implements Function<Vector, Vector> {

  private final int inSize;
  private final int outSize;

  private Layer inputLayer;
  private Layer outputLayer;

  public RangerNetwork(int inSize, int outSize) {
    this.inSize = inSize;
    this.outSize = outSize;
  }

  public RangerNetwork initialize() {
    inputLayer = new Layer(inSize);
    outputLayer = new Layer(outSize);
    Layer.link(inputLayer, outputLayer);
    return this;
  }

  public Vector estimate(Vector v) {
    inputLayer.loadActivation(v).propagateForward();
    return outputLayer.getActivation();
  }

  public static RangerNetwork fromJson(Json json) {
    checkState(json.hasKey("inSize"), "Ranger network couldn't parse JSON without inSize");
    checkState(json.hasKey("outSize"), "Ranger network couldn't parse JSON without outSize");
    return new RangerNetwork(json.getInt("inSize"), json.getInt("outSize"));
  }

  public Json toJson() {
    return Json.object()
        .with("inSize", inSize)
        .with("outSize", outSize);
  }

  @Override
  public Vector apply(Vector v) {
    return estimate(v);
  }
}
