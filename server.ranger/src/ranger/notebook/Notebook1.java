package ranger.notebook;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import ox.Log;
import ranger.data.LabeledRegressionDatapoint;
import ranger.data.TrainTestSplit;
import ranger.data.sets.BullseyeDataset;
import ranger.data.sets.Dataset;
import ranger.data.sets.RegressionDataset;
import ranger.randomforest.RandomForestRegressor;

public class Notebook1 {

  public static final int NUM_EXAMPLES = 1000;
  public static final double INPUT_NOISE = 0.005;
  public static final double OUTPUT_NOISE = 0.03;
  public static final double TRAINING_RATIO = 0.95;
  public static final int RANDOM_SEED = 42;

  // Hyperparameters
  public static final int NUM_TREES = 200;
  public static final int EXAMPLES_PER_TREE = (int) (NUM_EXAMPLES * TRAINING_RATIO); // N;\
  public static final int LEAF_SIZE = 5;
  public static final int MAX_DEPTH = 5;

  public final Random random = new Random(RANDOM_SEED);

  public static void main(String... args) {
    new Notebook1().run();
  }

  public void run() {

    // Just making sure we're making a reasonable dataset.
    experiment1();
    // Results: we found a bug in the dataset generation. Namely, we were using input noise with standard deviation 1;
    // it was ignoring the standard deviation argument!

    // Train a RandomForestRegressor on the bullseye data with a little noise.
    experiment2();
    // Took 20 seconds to train 200 trees (leaf size 5, max depth 5) on 950 examples. Root mean squared error was 0.26,
    // and that was with 0.005 input noise and 0.03 output noise (standard deviation).

  }

  public void experiment1() {
    Dataset dataset = BullseyeDataset.generateBullseyeDataset(10, 0.005, 0.03, random);
  }

  public void experiment2() {
    Dataset dataset = BullseyeDataset.generateBullseyeDataset(NUM_EXAMPLES, INPUT_NOISE, OUTPUT_NOISE, random);
    TrainTestSplit split = new TrainTestSplit(dataset).split(TRAINING_RATIO, random);
    RegressionDataset trainingSet = RegressionDataset.fromDataset(split.trainingSet);
    RegressionDataset testSet = RegressionDataset.fromDataset(split.testSet);
    Stopwatch watch = Stopwatch.createStarted();
    Log.debug("Training a RandomForestRegressor on the bullseye dataset.");
    RandomForestRegressor regressor = new RandomForestRegressor(NUM_TREES, EXAMPLES_PER_TREE, LEAF_SIZE, MAX_DEPTH,
        random).fit(trainingSet);
    Log.debug("Took %d milliseconds.", watch.elapsed(TimeUnit.MILLISECONDS));

    int testSetSize = testSet.size();
    double totalSquaredError = 0.0;
    for (LabeledRegressionDatapoint ldp : testSet) {
      double prediction = regressor.predict(ldp.datapoint);
      double actual = ldp.label;
      totalSquaredError += Math.pow(prediction - actual, 2);
      Log.debug("Prediction: %.2f, actual: %.2f", prediction, actual);
    }
    Log.debug("Root Mean Squared Error: %.2f", Math.sqrt(totalSquaredError / testSetSize));
  }
}
