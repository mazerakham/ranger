package ranger.randomforest;

import ranger.data.sets.Dataset;

public class DecisionTreeNode {

  private int leafSize;
  private int maxDepth;

  // Leaf characteristics. Leaves ultimately make predictions.
  private boolean isLeaf = false;
  private double mean;

  public DecisionTreeNode(int leafSize, int maxDepth) {
    this.leafSize = leafSize;
    this.maxDepth = maxDepth;
  }

  public DecisionTreeNode fit(Dataset dataset) {
    if (maxDepth == 0) {
      isLeaf = true;
      fitAsLeaf(dataset);
      return this;
    }
    this.mean = data
  }

  private void fitAsLeaf(Dataset dataset) {
    throw new UnsupportedOperationException();
  }
}
