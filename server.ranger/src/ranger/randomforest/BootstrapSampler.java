package ranger.randomforest;

import java.util.Random;

import ranger.data.sets.Dataset;

/**
 * Returns a random sample *with replacement* of a dataset.
 */
public class BootstrapSampler {

  private final Dataset dataset;

  private final int numExamplesPerSample;

  public BootstrapSampler(Dataset dataset, int numExamplesPerSample) {
    this.dataset = dataset;
    this.numExamplesPerSample = numExamplesPerSample;
  }

  public Dataset getSample(Random random) {
    Dataset ret = new Dataset();
    for (int i = 0; i < numExamplesPerSample; i++) {

    }
    return ret;
  }
}
