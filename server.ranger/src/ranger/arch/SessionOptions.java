package ranger.arch;

import static ox.util.Utils.parseEnum;

import ox.Json;
import ranger.data.sets.Dataset;

public class SessionOptions {

  public final Dataset.DatasetType datasetType;

  public final PlainNeuralNetworkSpecs neuralNetworkSpecs;

  public static SessionOptions fromJson(Json json) {
    String datasetTypeString = json.get("datasetType").toUpperCase();
    Dataset.DatasetType datasetType = parseEnum(datasetTypeString, Dataset.DatasetType.class);
    PlainNeuralNetworkSpecs specs = PlainNeuralNetworkSpecs.fromJson(json, datasetType.inSize, datasetType.outSize);
    return new SessionOptions(datasetType, specs);
  }

  private SessionOptions(Dataset.DatasetType datasetType, PlainNeuralNetworkSpecs specs) {
    this.datasetType = datasetType;
    this.neuralNetworkSpecs = specs;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Dataset Type: " + datasetType + "\n");
    sb.append("Neural Network Specs: " + neuralNetworkSpecs);
    return sb.toString();
  }
}
