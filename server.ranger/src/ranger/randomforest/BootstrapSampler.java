package ranger.randomforest;

import java.util.Random;

import ranger.data.sets.RegressionDataset;

/**
 * Returns a random sample *with replacement* of a dataset.
 */
public class BootstrapSampler {

  private final RegressionDataset dataset;

  private final int numExamplesPerSample;

  public BootstrapSampler(RegressionDataset dataset, int numExamplesPerSample) {
    this.dataset = dataset;
    this.numExamplesPerSample = numExamplesPerSample;
  }

  public RegressionDataset getSample(Random random) {
    RegressionDataset ret = new RegressionDataset();
    for (int i = 0; i < numExamplesPerSample; i++) {
      ret.add(dataset.get(random.nextInt(dataset.size())));
    }
    return ret;
  }

}
