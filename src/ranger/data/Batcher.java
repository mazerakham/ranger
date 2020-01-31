package ranger.data;

import static com.google.common.base.Preconditions.checkNotNull;

public class Batcher {

  private final Dataset dataset;
  private final int batchSize;

  public Batcher(Dataset dataset, int batchSize) {
    this.dataset = checkNotNull(dataset);
    this.batchSize = batchSize;
  }

  public Batch getBatch() {
    throw new UnsupportedOperationException();
  }
}
