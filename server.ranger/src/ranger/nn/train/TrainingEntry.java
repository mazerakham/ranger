package ranger.nn.train;

import ox.Json;
import ranger.nn.SingleLayerNeuralNetwork;

public class TrainingEntry {

  private final SingleLayerNeuralNetwork neuralNetwork;

  public TrainingEntry(SingleLayerNeuralNetwork neuralNetwork) {
    this.neuralNetwork = neuralNetwork.clone();
  }

  public Json toJson() {
    return neuralNetwork.toJson();
  }
}
