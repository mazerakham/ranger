package ranger.randomforest;

import ranger.data.sets.Dataset;
import ranger.math.Vector;

public class DecisionTree {

  private final int leafSize;

  private final int maxDepth;

  private DecisionTreeNode root = null;

  public DecisionTree(int leafSize, int maxDepth) {
    this.leafSize = leafSize;
    this.maxDepth = maxDepth;
  }

  public DecisionTree fit(Dataset dataset) {
    Vector exampleDatapoint = dataset.get(0).datapoint;
    Split best = null;
    this.root = new DecisionTreeNode(leafSize, maxDepth).fit(dataset);
    return this;
  }

}
