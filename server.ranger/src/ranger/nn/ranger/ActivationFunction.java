package ranger.nn.ranger;

import ranger.math.RangerMath;

import ranger.math.Vector;

public class ActivationFunction {

  public Vector apply(Vector v) {
    return RangerMath.relu(v);
  }
}
