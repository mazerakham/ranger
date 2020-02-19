package ranger.nn.ranger;

import ox.Json;

public class Signal {

  public final double value;
  public final double strength;

  public Signal(double value, double strength) {
    this.value = value;
    this.strength = strength;
  }

  @Override
  public String toString() {
    return String.format("%.2f, %.2f", value, strength);
  }

  public Json toJson() {
    return Json.array(value, strength);
  }
}
