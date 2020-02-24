package ranger.randomforest;

public class FeatureStatistics {

  public final double min;
  public final double max;
  public final boolean isOneHot;

  public FeatureStatistics(double min, double max, boolean isOneHot) {
    this.min = min;
    this.max = max;
    this.isOneHot = isOneHot;
  }
}
