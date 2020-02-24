package ranger.randomforest;

import static ox.util.Functions.map;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;

import ranger.math.RangerMath;

import ox.Json;
import ranger.data.LabeledRegressionDatapoint;
import ranger.math.Vector;

public class Split {

  private final Integer featureIndex;
  public final Double split;
  public final double totalVariance;

  // Trivial Split characteristics.
  public final boolean isTrivial;
  public final Double mean;

  public static Split trivialSplit(DecisionTreeRegressionDataset dataset) {
    List<Double> numbers = map(dataset, ldp -> ldp.label);
    double mean = RangerMath.mean(numbers);
    double variance = RangerMath.variance(numbers);
    return new Split(mean, variance);
  }

  public static Split atIndex(DecisionTreeRegressionDataset sortedDataset, int datasetIndex, int featureIndex) {
    double split = (sortedDataset.get(datasetIndex).datapoint.getEntry(featureIndex)
        + sortedDataset.get(datasetIndex + 1).datapoint.getEntry(featureIndex)) / 2.0;
    double leftVariance = RangerMath.variance(map(sortedDataset.subList(0, datasetIndex), ldp -> ldp.label));
    double rightVariance = RangerMath.variance(map(sortedDataset.subList(datasetIndex, sortedDataset.size()), ldp -> ldp.label));
    return new Split(featureIndex, split, leftVariance + rightVariance);
  }

  /**
   * Compute the lowest-variance legal split. Legal meaning, neither side is smaller than {@code leafSize}.
   */
  public static Split computeOptimalSplit(DecisionTreeRegressionDataset dataset, int leafSize) {
    int numFeatures = dataset.numFeatures;
    Split ret = null;
    for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++) {
      Split candidate = computeOptimalSplit(dataset, leafSize, featureIndex);
      if (candidate.isBetterThan(ret)) {
        ret = candidate;
      }
    }
    return ret;
  }

  // Trivial split constructor.
  private Split(double mean, double variance) {
    this.featureIndex = null;
    this.split = null;
    this.mean = mean;
    this.totalVariance = variance;
    this.isTrivial = true;
  }

  public Split(int featureIndex, double split, double variance) {
    this.featureIndex = featureIndex;
    this.split = split;
    this.totalVariance = variance;
    this.isTrivial = false;
    this.mean = null;
  }

  public boolean isBetterThan(Split that) {
    if (that == null) {
      return true;
    } else {
      return this.totalVariance < that.totalVariance;
    }
  }

  public boolean isLeft(Vector v) {
    double featureVal = v.getEntry(featureIndex);
    return featureVal < split;
  }


  private static Split computeOptimalSplit(DecisionTreeRegressionDataset dataset, int leafSize, int featureIndex) {
    TreeSet<LabeledRegressionDatapoint> thingy = Sets.newTreeSet(dataset);
    DecisionTreeRegressionDataset sorted = dataset.clone();
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

  public Json toJson() {
    if (isTrivial) {
      return Json.array("trivial");
    } else {
      return Json.object()
          .with("featureIndex", featureIndex)
          .with("split", split);
    }
    
  }
}
