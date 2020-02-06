package ranger.arch;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import com.google.common.collect.Lists;

import ox.Json;

public class PlainNeuralNetworkSpecs {

  public final int numLayers;

  public final List<Integer> layerSizes;

  public static PlainNeuralNetworkSpecs fromJson(Json json, int inSize, int outSize) {
    int numLayers = json.getInt("numHiddenLayers") + 2; // input and output are counted as layers.
    List<Integer> sizes = Lists.newArrayList(json.getJson("layerSizes").asIntArray());
    sizes.add(0, inSize);
    sizes.add(outSize);
    checkState(sizes.size() == numLayers);
    return new PlainNeuralNetworkSpecs(numLayers, sizes);
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
