package ranger.data;

import ox.Json;
import ranger.math.Vector;

public class LabeledDatapoint {

  public final Vector datapoint;
  public final Vector label;

  public static LabeledDatapoint fromJson(Json json) {
    return new LabeledDatapoint(
        Vector.fromJson(json.getJson("datapoint")),
        Vector.fromJson(json.getJson("label")));
  }

  public LabeledDatapoint(Vector datapoint, Vector label) {
    this.datapoint = datapoint;
    this.label = label;
  }

  public Json toJson() {
    return Json.object()
        .with("datapoint", datapoint.toJson())
        .with("label", label.toJson());
  }

  @Override
  public String toString() {
    return datapoint.toString() + "\n" + label.toString();
  }
}
