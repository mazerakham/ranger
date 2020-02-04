package ranger.nn.train;

import static com.google.common.base.Preconditions.checkNotNull;

import ox.Log;
import ranger.data.Batch;
import ranger.data.Batcher;
import ranger.math.Vector;
import ranger.nn.NeuralNetwork;
import ranger.nn.NeuralNetworkGradient;

public class SGDTrainer {

  private final Batcher batcher;
  private final double learningRate;
  private final int numBatches;

  private boolean saveHistory = false;
  private TrainingHistory history = new TrainingHistory();

  public SGDTrainer(Batcher batcher, double learningRate, int numBatches) {
    this.batcher = checkNotNull(batcher);
    this.learningRate = learningRate;
    this.numBatches = numBatches;
  }

  public SGDTrainer saveHistory() {
    this.saveHistory = true;
    return this;
  }

  public TrainingHistory getHistory() {
    return this.history;
  }

  public void train(NeuralNetwork neuralNetwork) {
    for (int i = 0; i < numBatches; i++) {
      Batch batch = batcher.getBatch();
      NeuralNetworkGradient gradient = computeBatchGradient(neuralNetwork, batch);
      if (i % (numBatches / 100) == 0) {
        Log.debug("gradient sizes:");
        Log.debug(gradient + "\n");
      }
      if (saveHistory) {
        history.addEntry(new TrainingEntry(neuralNetwork));
      }
      neuralNetwork.update(gradient, learningRate);
    }
  }

  private NeuralNetworkGradient computeBatchGradient(NeuralNetwork neuralNetwork, Batch batch) {
    NeuralNetworkGradient gradient = new NeuralNetworkGradient(neuralNetwork);
    for (int i = 0; i < batch.size(); i++) {
      Vector datapoint = batch.getDatapoint(i);
      Vector label = batch.getLabel(i);
      gradient = gradient.plus(neuralNetwork.lossGradient(datapoint, label));
    }
    gradient.scale(batch.size());
    return gradient;
  }
}
