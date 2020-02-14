package ranger.nn.ranger;

import java.util.ArrayList;

import ranger.math.Vector;

public class SignalVector {

  public ArrayList<Signal> signals = new ArrayList<>();

  /**
   * Converts a vector to corresponding signal of strength 1.
   */
  public static SignalVector saturatedSignal(Vector v) {
    SignalVector ret = new SignalVector();
    for (int i = 0; i < v.size(); i++) {
      ret.signals.add(new Signal(v.getEntry(i), 1.0));
    }
    return ret;
  }

  public int size() {
    return signals.size();
  }

  public Vector values() {
    return Vector.fromFunction(size(), i -> signals.get(i).value);
  }

}
