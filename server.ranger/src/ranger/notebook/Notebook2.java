package ranger.notebook;

import java.io.File;
import java.util.Random;

import ox.IO;
import ranger.data.Batcher;
import ranger.data.TrainTestSplit;
import ranger.data.sets.BullseyeDataset;
import ranger.data.sets.Dataset;
import ranger.nn.Evaluator;
import ranger.nn.NeuralNetwork;
import ranger.nn.train.SGDTrainer;
import ranger.nn.train.TrainingHistory;

public class Notebook2 {

  private static final int RANDOM_SEED = 42;

  private static final int NUM_DATAPOINTS = 10_000;
  private static final double INPUT_NOISE = 0.04;
  private static final double OUTPUT_NOISE = 0.04;

  private static final double TRAINING_RATIO = 0.95;

  private static final int HIDDEN_LAYER_SIZE = 20;

  private static final int BATCH_SIZE = 100;
  private static final int NUM_BATCHES = 1_000;
  private static final double LEARNING_RATE = 0.005;

  private final Random random = new Random(RANDOM_SEED);

  public static void main(String... args) {
    new Notebook2().run();
  }

  public void run() {
    experiment1();
  }

  public void experiment1() {
    Dataset bullseyeDataset = BullseyeDataset.generateBullseyeDataset(NUM_DATAPOINTS, INPUT_NOISE, OUTPUT_NOISE,
        random);

    TrainTestSplit splitter = new TrainTestSplit(bullseyeDataset).split(TRAINING_RATIO, random);
    Dataset trainingSet = splitter.trainingSet;
    Dataset testSet = splitter.testSet;

    NeuralNetwork neuralNetwork = new NeuralNetwork(2, HIDDEN_LAYER_SIZE, 1).randomlyInitialize(random);

    Batcher batcher = new Batcher(trainingSet, BATCH_SIZE);
    SGDTrainer trainer = new SGDTrainer(batcher, LEARNING_RATE, NUM_BATCHES).saveHistory();
    trainer.train(neuralNetwork);

    new Evaluator().evaluate(neuralNetwork, testSet);

    TrainingHistory history = trainer.getHistory();
    IO.from(history.toJson()).to(new File("history1.json"));
  }
}
