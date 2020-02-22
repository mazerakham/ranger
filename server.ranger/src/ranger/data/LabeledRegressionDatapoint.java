package ranger.data;

import ranger.math.Vector;

public class LabeledRegressionDatapoint {

  public final Vector datapoint;

  public final double label;

  public LabeledRegressionDatapoint(Vector datapoint, double label) {
    this.datapoint = datapoint;
    this.label = label;
  }

  @Override
  public String toString() {
    return String.format("[ %s , %.2f ]", datapoint, label);
  }
}
