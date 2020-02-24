package ranger.randomforest;

import static com.google.common.base.Preconditions.checkState;

import ox.Json;
import ranger.data.sets.RegressionDataset;
import ranger.math.Vector;

public class DecisionTreeNode {

  private int leafSize;
  private int maxDepth;

  // Node characteristics.
  Split split = null;
  DecisionTreeNode left = null;
  DecisionTreeNode right = null;

  // Leaf characteristics. Leaves ultimately make predictions.
  private boolean isLeaf = false;
  private double mean;

  public DecisionTreeNode(int leafSize, int maxDepth) {
    this.leafSize = leafSize;
    this.maxDepth = maxDepth;
  }

  public DecisionTreeNode fit(RegressionDataset dataset, FastSplitter splitter) {
    checkState(dataset.size() >= leafSize,
        String.format("Cannot make a decision tree node of size %d when leafSize is %d", dataset.size(), leafSize));
    if (maxDepth == 0) {
      isLeaf = true;
      fitAsLeaf(dataset);
      return this;
    }
    splitter.computeOptimalSplit(dataset, leafSize);
    if (split.isTrivial || split.getLeft(dataset).size() < leafSize || split.getRight(dataset).size() < leafSize) {
      isLeaf = true;
      fitAsLeaf(dataset);
      return this;
    }
    left = new DecisionTreeNode(leafSize, maxDepth - 1).fit(split.getLeft(dataset), splitter);
    right = new DecisionTreeNode(leafSize, maxDepth - 1).fit(split.getRight(dataset), splitter);
    return this;
  }

  public double predict(Vector v) {
    if (isLeaf) {
      return mean;
    } else if (split.isLeft(v)) {
      return left.predict(v);
    } else {
      return right.predict(v);
    }
  }

  private void fitAsLeaf(RegressionDataset dataset) {
    split = Split.trivialSplit(dataset);
    mean = split.mean;
    left = null;
    right = null;
  }

  public Json toJson() {
    if (isLeaf) {
      return Json.object().with("mean", mean);
    } else {
      return Json.object().with("split", this.split.toJson()).with("left", this.left.toJson()).with("right",
          this.right.toJson());
    }
  }
}
