package ranger.randomforest;

import java.util.Random;

/**
 * Returns a random sample *with replacement* of a dataset.
 */
public class BootstrapSampler {

  private final DecisionTreeRegressionDataset dataset;

  private final int numExamplesPerSample;

  public BootstrapSampler(DecisionTreeRegressionDataset dataset, int numExamplesPerSample) {
    this.dataset = dataset;
    this.numExamplesPerSample = numExamplesPerSample;
  }

  public DecisionTreeRegressionDataset getSample(Random random) {
    DecisionTreeRegressionDataset ret = new DecisionTreeRegressionDataset();
    for (int i = 0; i < numExamplesPerSample; i++) {
      ret.add(dataset.get(random.nextInt(dataset.size())));
    }
    return ret;
  }

}
