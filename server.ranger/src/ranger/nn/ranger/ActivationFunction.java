package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;

import ranger.math.RangerMath;

import ranger.math.Vector;

public class ActivationFunction {

  public Vector apply(Vector v) {
    return RangerMath.relu(v);
  }

  public static ActivationFunction fromJson(String jsonString) {
    if (jsonString == null) {
      return null;
    }
    checkState(jsonString.equals("relu"), "ActivationFunction only supports relu");
    return new ActivationFunction();
  }

  public String toJson() {
    return "relu";
  }
}
