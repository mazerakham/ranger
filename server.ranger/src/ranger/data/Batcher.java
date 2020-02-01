package ranger.data;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Converts a dataset into training batches.
 * 
 * Warning: for now, this doesn't do shuffling of the dataset, which is bad. (TODO)
 */
public class Batcher {

  private final Dataset dataset;
  private final int batchSize;
  private final int size;

  private int ix = 0;

  public Batcher(Dataset dataset, int batchSize) {
    this.dataset = checkNotNull(dataset);
    this.batchSize = batchSize;
    this.size = dataset.size();
  }

  public Batch getBatch() {
    Batch ret = new Batch();
    for (int i = 0; i < batchSize; i++) {
      ret.add(dataset.get((ix++) % size));
    }
    return ret;
  }

}
