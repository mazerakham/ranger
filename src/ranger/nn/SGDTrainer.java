package ranger.nn;

import ranger.data.Batch;
import ranger.data.Batcher;
import ranger.data.Dataset;

public class SGDTrainer {

  private final int numBatches = 10;
  private final int batchSize = 50;

  private final Dataset dataset;
  private final NeuralNetwork neuralNetwork;

  public SGDTrainer(Dataset dataset, NeuralNetwork neuralNetwork) {
    this.dataset = dataset;
    this.neuralNetwork = neuralNetwork;
  }

  public void train() {
    Batcher batcher = new Batcher(dataset, batchSize);
    for (int i = 0; i < numBatches; i++) {
      Batch batch = batcher.getBatch();
      NeuralNetworkGradient gradient = neuralNetwork.computeBatchGradient(batch.datapoints, batch.labels);
    }
  }
}
