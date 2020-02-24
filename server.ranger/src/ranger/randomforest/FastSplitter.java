package ranger.randomforest;

import static ox.util.Functions.sum;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Maps;

import ranger.data.LabeledRegressionDatapoint;
import ranger.data.sets.RegressionDataset;
import ranger.math.Vector;

public class FastSplitter {

  public int numFeatures;

  private Map<Integer, FeatureStatistics> featureStatistics = Maps.newHashMap();

  private double leftSumSq;
  private double leftSum;
  private double rightSumSq;
  private double rightSum;

  public FastSplitter computeStatistics(RegressionDataset dataset) {
    LabeledRegressionDatapoint example = dataset.get(0);
    Vector datapoint = example.datapoint;
    numFeatures = datapoint.size();
    for (int i = 0; i < numFeatures; i++) {
      featureStatistics.put(i, computeStatistics(dataset, i));
    }
    return this;
  }

  private FeatureStatistics computeStatistics(RegressionDataset dataset, int featureIndex) {
    boolean isOneHot = true;
    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;
    for (int i = 0; i < dataset.size(); i++) {
      Vector datapoint = dataset.get(i).datapoint;
      double val = datapoint.getEntry(featureIndex);
      if (val != 0.0 && val != 1.0) {
        isOneHot = false;
      }
      min = Math.min(min, val);
      max = Math.max(max, val);
    }
    return new FeatureStatistics(min, max, isOneHot);
  }

  public Split computeOptimalSplit(RegressionDataset dataset, int leafSize) {
    Split ret = null;
    for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++) {
      Split candidate = computeOptimalSplit(dataset, leafSize, featureIndex);
      if (candidate.isBetterThan(ret)) {
        ret = candidate;
      }
    }
    return ret;
  }

  private Split computeOptimalSplit(RegressionDataset dataset, int leafSize, int featureIndex) {
    FeatureStatistics statistics = featureStatistics.get(featureIndex);
    if (statistics.isOneHot) {
      return Split.oneHot(dataset, featureIndex);
    }
    Collections.sort(dataset, (ldp1, ldp2) -> ComparisonChain.start().compare(ldp1.datapoint.getEntry(featureIndex), ldp2.datapoint.getEntry(featureIndex)).result());
    Split ret = Split.trivialSplit(dataset);

    leftSumSq = 0.0;
    leftSum = 0.0;
    rightSumSq = sum(dataset, ldp -> Math.pow(ldp.label, 2));
    rightSum = sum(dataset, ldp -> ldp.label);

    for (int i = 0; i < dataset.size(); i++) {
      Split candidate = splitAt(dataset, leafSize, i, featureIndex);
      if (candidate.isLegal() && candidate.isBetterThan(ret)) {
        ret = candidate;
      }
    }
    return ret;
  }

  private Split splitAt(RegressionDataset dataset, int leafSize, int index, int featureIndex) {
    double thisVal = dataset.get(index).label;
    leftSumSq = leftSumSq + thisVal * thisVal;
    rightSumSq = rightSumSq - thisVal * thisVal;
    leftSum = leftSum + thisVal;
    rightSum = rightSum - thisVal;

    int leftSize = index + 1;
    int rightSize = dataset.size() - leftSize;
    if (leftSize < leafSize || rightSize < leafSize) {
      return Split.ILLEGAL_SPLIT;
    }

    double thisFeatureVal = dataset.get(index).datapoint.getEntry(featureIndex);
    double nextFeatureVal = index + 1 < dataset.size() ? dataset.get(index + 1).datapoint.getEntry(featureIndex)
        : thisFeatureVal + 0.01;
    double split = (thisFeatureVal + nextFeatureVal) / 2.0;

    double leftVariance = 1.0 / leftSize * leftSumSq - 1.0 / (leftSize * leftSize) * leftSum * leftSum;
    double rightVariance = 1.0 / rightSize * rightSumSq - 1.0 / (rightSize * rightSize) * rightSum * rightSum;
    double score = leftSize * leftVariance + rightSize * rightVariance;
    double leftMean = 1.0 / leftSize * leftSum;
    double rightMean = 1.0 / rightSize * rightSum;
    
    return new Split(featureIndex, split, score);
  }

}
