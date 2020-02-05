package ranger.nn.train;

import ox.Json;
import ranger.nn.NeuralNetwork;

public class TrainingEntry {

  private final NeuralNetwork neuralNetwork;

  public TrainingEntry(NeuralNetwork neuralNetwork) {
    this.neuralNetwork = neuralNetwork.clone();
  }

  public Json toJson() {
    return neuralNetwork.toJson();
  }
}
