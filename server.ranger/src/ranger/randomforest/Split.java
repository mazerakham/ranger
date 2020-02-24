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
  public final double cost;

  // Trivial Split characteristics.
  public final boolean isTrivial;
  public final Double mean;

  // IllegalSplit
  public static final Split ILLEGAL_SPLIT = new Split(0.0, 0.0);
  static {
    ILLEGAL_SPLIT.illegal = true;
  }
  private boolean illegal = false;

  public static Split trivialSplit(RegressionDataset dataset) {
    List<Double> numbers = map(dataset, ldp -> ldp.label);
    double mean = RangerMath.mean(numbers);
    double variance = RangerMath.variance(numbers);
    return new Split(mean, variance * dataset.size());
  }

  public static Split oneHot(RegressionDataset dataset, int featureIndex) {
    throw new UnsupportedOperationException();
  }

  // Trivial split constructor.
  private Split(double mean, double cost) {
    this.featureIndex = null;
    this.split = null;
    this.mean = mean;
    this.cost = cost;
    this.isTrivial = true;
  }

  public Split(int featureIndex, double split, double cost) {
    this.featureIndex = featureIndex;
    this.split = split;
    this.cost = cost;
    this.isTrivial = false;
    this.mean = null;
  }

  public boolean isBetterThan(Split that) {
    if (that == null) {
      return true;
    } else {
      return this.cost < that.cost;
    }
  }

  public boolean isLegal() {
    return !this.illegal;
  }

  public boolean isLeft(Vector v) {
    double featureVal = v.getEntry(featureIndex);
    return featureVal < split;
  }

  public RegressionDataset getLeft(RegressionDataset dataset) {
    return new RegressionDataset(filter(dataset, ldp -> ldp.datapoint.getEntry(featureIndex) < split));
  }

  public RegressionDataset getRight(RegressionDataset dataset) {
    return new RegressionDataset(filter(dataset, ldp -> ldp.datapoint.getEntry(featureIndex) >= split));
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
