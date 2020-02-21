package ranger.randomforest;

public class Split {

  private final int featureIndex;
  private final double split;
  private final double variance;

  public Split(int featureIndex, double split, double variance) {
    this.featureIndex = featureIndex;
    this.split = split;
    this.variance = variance;
  }

  public boolean isBetterThan(Split that) {
    if (that == null) {
      return true;
    } else {
      return this.variance < that.variance;
    }
  }
}
