package ranger.nn.ranger;

import java.util.function.Function;

import ranger.math.RangerMath;

public class ActivationFunction {

  public static final ActivationFunction IDENTITY = new ActivationFunction("identity", 
      d -> d, d -> 1.0);

  public static final ActivationFunction RELU = new ActivationFunction("relu", d -> RangerMath.relu(d),
      d -> d > 0.0 ? 1.0 : 0.0);

  public final Function<Double, Double> activationFunction;

  public final Function<Double, Double> derivative;

  public final String name;

  private ActivationFunction(String name, Function<Double, Double> activationFunction,
      Function<Double, Double> derivative) {
    this.name = name;
    this.activationFunction = activationFunction;
    this.derivative = derivative;
  }

  public double apply(double d) {
    return this.activationFunction.apply(d);
  }

  public double derivativeAt(double d) {
    return this.derivative.apply(d);
  }

  public static ActivationFunction fromJson(String jsonString) {
    if (jsonString == null) {
      return null;
    } else if (jsonString.equals("relu")) {
      return RELU;
    } else if (jsonString.equals("identity")) {
      return IDENTITY;
    } else {
      throw new RuntimeException("ActivationFunction " + jsonString + " is not supported.");
    }
  }

  public String toJson() {
    return this.name;
  }
}
