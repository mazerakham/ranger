package ranger.randomforest;

import ox.Json;
import ranger.data.sets.RegressionDataset;
import ranger.math.Vector;

public class DecisionTree {

  private final int leafSize;

  private final int maxDepth;

  private DecisionTreeNode root = null;

  public DecisionTree(int leafSize, int maxDepth) {
    this.leafSize = leafSize;
    this.maxDepth = maxDepth;
  }

  public DecisionTree fit(RegressionDataset dataset) {
    this.root = new DecisionTreeNode(leafSize, maxDepth).fit(dataset);
    return this;
  }

  public double predict(Vector v) {
    return root.predict(v);
  }

  public Json toJson() {
    return root.toJson();
  }

}
