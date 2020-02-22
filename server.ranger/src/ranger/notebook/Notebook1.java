package ranger.notebook;

import java.util.Random;

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
    experiment1();
    experiment2();

  }

  public void experiment1() {
    Dataset dataset = BullseyeDataset.generateBullseyeDataset(10, 0.005, 0.03, random);
  }

  public void experiment2() {
    Dataset dataset = BullseyeDataset.generateBullseyeDataset(NUM_EXAMPLES, INPUT_NOISE, OUTPUT_NOISE, random);
    TrainTestSplit split = new TrainTestSplit(dataset).split(TRAINING_RATIO, random);
    RegressionDataset trainingSet = RegressionDataset.fromDataset(split.trainingSet);
    RegressionDataset testSet = RegressionDataset.fromDataset(split.testSet);
    RandomForestRegressor regressor = new RandomForestRegressor(NUM_TREES, EXAMPLES_PER_TREE, LEAF_SIZE, MAX_DEPTH,
        random).fit(trainingSet);
    for (LabeledRegressionDatapoint ldp : testSet) {
      double prediction = regressor.predict(ldp.datapoint);
      double actual = ldp.label;
      Log.debug("Prediction: %.2f, actual: %.2f", prediction, actual);
    }
  }
}
