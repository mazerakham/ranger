package ranger.nn;

import java.util.ArrayList;
import java.util.List;

import ranger.math.Vector;

public class PlainActivation {

  private List<Vector> preActivations = new ArrayList<>();

  public void addPreActivation(Vector preActivation) {
    this.preActivations.add(preActivation);
  }

  public Vector getPreActivation(int n) {
    return preActivations.get(n);
  }
}
