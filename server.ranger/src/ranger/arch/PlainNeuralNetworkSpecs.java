package ranger.arch;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import ox.Json;

public class PlainNeuralNetworkSpecs {

  public final int numLayers;

  public final List<Integer> layerSizes;

  public static PlainNeuralNetworkSpecs fromJsonInferIOLayers(Json json) {
    int numHiddenLayers = json.getInt("numHiddenLayers");
    int numLayers = numHiddenLayers + 2;
    List<Integer> sizes = json.getJson("hiddenLayerSizes").asIntArray();
    sizes.add(0, 2); // hard-code 2-dimensional input, 1-dimensional output for now.
    sizes.add(1); // TODO: Generalize this to use the dataset to correctly infer IO size.
    checkState(sizes.size() == numLayers);
    return new PlainNeuralNetworkSpecs(numLayers, sizes);
  }

  public static PlainNeuralNetworkSpecs fromJson(Json json) {
    if (json.hasKey("numHiddenLayers")) {
      return fromJsonInferIOLayers(json);
    } else {
      return new PlainNeuralNetworkSpecs(json.getInt("numLayers"), json.getJson("layerSizes").asIntArray());
    }
  }

  public PlainNeuralNetworkSpecs(int numLayers, List<Integer> sizes) {
    this.numLayers = numLayers;
    this.layerSizes = sizes;
  }

  public Json toJson() {
    return Json.object()
        .with("numLayers", numLayers)
        .with("layerSizes", Json.array(layerSizes));
  }
}
