package ranger.data;

import ranger.math.Vector;

public class LabeledDatapoint {

  public final Vector datapoint;
  public final Vector label;

  public LabeledDatapoint(Vector datapoint, Vector label) {
    this.datapoint = datapoint;
    this.label = label;
  }

  @Override
  public String toString() {
    return datapoint.toString() + "\n" + label.toString();
  }
}
