package ranger.nn.train;

import static com.google.common.base.Preconditions.checkNotNull;

import ranger.data.Batch;
import ranger.data.Batcher;
import ranger.nn.PlainNeuralNetwork;
import ranger.nn.PlainNeuralNetworkGradient;

public class SGDTrainer {

  private final Batcher batcher;
  private final double learningRate;
  private final int numBatches;

  public SGDTrainer(Batcher batcher, double learningRate, int numBatches) {
    this.batcher = checkNotNull(batcher);
    this.learningRate = learningRate;
    this.numBatches = numBatches;
  }

  public void train(PlainNeuralNetwork neuralNetwork) {
    for (int i = 0; i < numBatches; i++) {
      Batch batch = batcher.getBatch();
      PlainNeuralNetworkGradient gradient = new PlainNeuralNetworkGradient(neuralNetwork.specs);
      for (int j = 0; j < batch.size(); j++) {
        gradient.add(neuralNetwork.lossGradient(batch.getDatapoint(j), batch.getLabel(j)));
      }
      gradient.scale(1.0 / batch.size());
      neuralNetwork.update(gradient, learningRate);
    }
  }
}

