package ranger.randomforest;

import static com.google.common.base.Preconditions.checkState;

import java.util.Collections;

import com.google.common.collect.ComparisonChain;

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

  public DecisionTreeNode fit(RegressionDataset dataset) {
    checkState(dataset.size() >= leafSize,
        String.format("Cannot make a decision tree node of size %d when leafSize is %d", dataset.size(), leafSize));
    if (maxDepth == 0) {
      isLeaf = true;
      fitAsLeaf(dataset);
      return this;
    }
    split = computeOptimalSplit(dataset);
    if (split.isTrivial || split.getLeft(dataset).size() < leafSize || split.getRight(dataset).size() < leafSize) {
      isLeaf = true;
      fitAsLeaf(dataset);
      return this;
    }
    left = new DecisionTreeNode(leafSize, maxDepth - 1).fit(split.getLeft(dataset));
    right = new DecisionTreeNode(leafSize, maxDepth - 1).fit(split.getRight(dataset));
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

  /**
   * The heart of the algorithm: pick a single feature and a single value, and split so as to minimize the total
   * mean-squared error. (Equivalently, minimize the variance on both sides.)
   */
  private Split computeOptimalSplit(RegressionDataset dataset) {
    Vector exampleVector = dataset.get(0).datapoint;
    Split ret = null;
    for (int featureIndex = 0; featureIndex < exampleVector.size(); featureIndex++) {
      Split candidate = computeOptimalSplit(dataset, featureIndex);
      if (candidate.isBetterThan(ret)) {
        ret = candidate;

      }
    }
    return ret;
  }

  // Compute the lowest-variance legal split. Legal meaning, neither side is smaller than {@code leafSize}.
  private Split computeOptimalSplit(RegressionDataset dataset, int featureIndex) {
    RegressionDataset sorted = new RegressionDataset(dataset);
    Collections.sort(sorted, (a, b) -> ComparisonChain.start()
        .compare(a.datapoint.getEntry(featureIndex), b.datapoint.getEntry(featureIndex)).result());
    Split ret = Split.trivialSplit(sorted);
    for (int i = leafSize; i < sorted.size() - leafSize; i++) {
      Split candidate = Split.atIndex(sorted, i, featureIndex);
      if (candidate.isBetterThan(ret)) {
        ret = candidate;
      }
    }
    return ret;
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
