package ranger.data;

import ranger.math.Matrix;
import ranger.math.Vector;

public class Batch {

  public final Matrix datapoints;

  public final Matrix labels;

  public Batch() {
    datapoints = new Matrix();
    labels = new Matrix();
  }

  public void add(LabeledDatapoint ldp) {
    datapoints.addRowVector(ldp.datapoint);
    labels.addRowVector(ldp.label);
  }

  public Vector getDatapoint(int i) {
    return datapoints.getRow(i);
  }

  public Vector getLabel(int i) {
    return labels.getRow(i);
  }

  public int size() {
    return datapoints.height();
  }
}
