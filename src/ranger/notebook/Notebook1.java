package ranger.notebook;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ranger.math.RangerMath;

import ox.Log;
import ranger.data.Batcher;
import ranger.data.Dataset;
import ranger.data.TrainTestSplit;
import ranger.math.Vector;
import ranger.nn.NeuralNetwork;
import ranger.nn.SGDTrainer;

public class Notebook1 {

  private static final int RANDOM_SEED = 42;

  private static final double INPUT_NOISE = 0.2;
  private static final double OUTPUT_NOISE = 0.2;

  private static final int NUM_DATAPOINTS = 10_000;
  private static final double TRAINING_RATIO = 0.95;

  private static final int NUM_BATCHES = 1000;
  private static final int BATCH_SIZE = 50;
  private static final double LEARNING_RATE = 0.01;

  private Random random = new Random(RANDOM_SEED);
  private Dataset basicDataset;
  private Dataset trainingDataset;
  private Dataset testDataset;

  private NeuralNetwork neuralNetwork;

  public static void main(String... args) {
    new Notebook1().run();
  }

  public void run() {
    // Hello world for the Ranger project!
    experiment1();

    // Generate some xor datapoints with noise.
    experiment2();

    // Split the dataset into train and test.
    experiment3();

    // Fit a 2-10-1 neural network to the training data.
    experiment4();

    // Test the neural network on the test data.
    experiment5();

  }

  // Hello world for the Ranger project!
  public void experiment1() {
    Log.debug("Running experiment 1.");
    Log.debug("Hello world!");
  }

  // Generate some xor datapoints with noise.
  public void experiment2() {
    Log.debug("Running experiment 2.");
    basicDataset = Dataset.generateBasicDataset(NUM_DATAPOINTS, INPUT_NOISE, OUTPUT_NOISE, random);
    for (int i = 0; i < 5; i++) {
      Log.debug(basicDataset.get(i));
    }
  }

  // Split the dataset into train and test.
  public void experiment3() {
    Log.debug("Running experiment 3.");
    TrainTestSplit splitter = new TrainTestSplit(basicDataset).split(TRAINING_RATIO, random);
    trainingDataset = splitter.trainingSet;
    testDataset = splitter.testSet;
    Log.debug("Training Set first 5:");
    for (int i = 0; i < 5; i++) {
      Log.debug(trainingDataset.get(i));
    }
    Log.debug("Test Set first 5:");
    for (int i = 0; i < 5; i++) {
      Log.debug(testDataset.get(i));
    }
  }

  // Fit a 2-10-1 neural network to the training data.
  public void experiment4() {
    Log.debug("Running experiment 4.");
    neuralNetwork = new NeuralNetwork(2, 8, 1).randomlyInitialize(random);
    new SGDTrainer(new Batcher(trainingDataset, BATCH_SIZE), LEARNING_RATE, NUM_BATCHES).train(neuralNetwork);
    Log.debug(neuralNetwork);
  }

  // Test the neural network on the test data.
  public void experiment5() {
    Log.debug("Running experiment 5.");
    List<Double> errors = new ArrayList<>();
    for (int i = 0; i < testDataset.size(); i++) {
      Vector prediction = neuralNetwork.estimate(testDataset.get(i).datapoint);
      errors.add(prediction.distance(testDataset.get(i).label));
    }
    double averageError = RangerMath.average(errors);
    Log.debug("Typical errors:");
    for (int i = 0; i < 5; i++) {
      Log.debug(errors.get(i));
    }
    Log.debug("Average error:");
    Log.debug(averageError);
  }

}
