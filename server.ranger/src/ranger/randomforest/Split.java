package ranger.randomforest;

import static ox.util.Functions.filter;
import static ox.util.Functions.map;

import java.util.List;

import ranger.math.RangerMath;

import ox.Json;
import ranger.data.sets.RegressionDataset;
import ranger.math.Vector;

public class Split {

  private final Integer featureIndex;
  public final Double split;
  public final double totalVariance;

  // Trivial Split characteristics.
  public final boolean isTrivial;
  public final Double mean;

  public static Split trivialSplit(RegressionDataset dataset) {
    List<Double> numbers = map(dataset, ldp -> ldp.label);
    double mean = RangerMath.mean(numbers);
    double variance = RangerMath.variance(numbers);
    return new Split(mean, variance);
  }

  public static Split atIndex(RegressionDataset sortedDataset, int datasetIndex, int featureIndex) {
    double split = (sortedDataset.get(datasetIndex).datapoint.getEntry(featureIndex)
        + sortedDataset.get(datasetIndex + 1).datapoint.getEntry(featureIndex)) / 2.0;
    double leftVariance = RangerMath.variance(map(sortedDataset.subList(0, datasetIndex), ldp -> ldp.label));
    double rightVariance = RangerMath.variance(map(sortedDataset.subList(datasetIndex, sortedDataset.size()), ldp -> ldp.label));
    return new Split(featureIndex, split, leftVariance + rightVariance);
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

  public RegressionDataset getLeft(RegressionDataset dataset) {
    return new RegressionDataset(filter(dataset, ldp -> ldp.datapoint.getEntry(featureIndex) < split));
  }

  public RegressionDataset getRight(RegressionDataset dataset) {
    return new RegressionDataset(filter(dataset, ldp -> ldp.datapoint.getEntry(featureIndex) >= split));
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
