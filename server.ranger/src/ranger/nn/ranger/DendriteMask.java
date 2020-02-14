package ranger.nn.ranger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ranger.math.RangerMath;

import ox.Json;
import ox.Log;
import ranger.math.Vector;

/**
 * Generally, the previous layer will be a large vector, and a neuron in this layer is only paying attention to a small
 * portion of the previous layer. This mask represents that.
 */
public class DendriteMask {

  private List<Integer> mask = new ArrayList<>();

  public DendriteMask(List<Integer> mask) {
    this.mask = mask;
  }

  public static DendriteMask randomMask(Layer layer, double proba, Random random) {
    List<Integer> mask = new ArrayList<>();
    for (int i = 0; i < layer.size(); i++) {
      if (random.nextDouble() < proba) {
        mask.add(i);
      }
    }
    return new DendriteMask(mask);
  }

  public static DendriteMask fullMask(Layer layer) {
    return new DendriteMask(RangerMath.range(layer.size()));
  }

  public SignalVector mask(SignalVector vector) {
    SignalVector ret = new SignalVector();
    for (int i = 0; i < size(); i++) {
      try {
        ret.signals.add(vector.signals.get(mask.get(i)));
      } catch (Exception e) {
        Log.debug("Got an error on dendrite mask:");
        Log.debug(this.toJson().prettyPrint());
        Log.debug("With incoming vector of size: %d", vector.size());
        throw new RuntimeException();
      }
    }
    return ret;
  }

  public Vector unMask(Vector vector, int size) {
    Vector ret = Vector.zeros(size);
    for (int i = 0; i < this.mask.size(); i++) {
      ret.setEntry(mask.get(i), vector.getEntry(i));
    }
    return ret;
  }

  public void add(int ix) {
    this.mask.add(ix);
  }

  public int size() {
    return mask.size();
  }

  public boolean contains(int neuronNumber) {
    for (int i = 0; i < mask.size(); i++) {
      if (mask.get(i) == neuronNumber) {
        return true;
      }
    }
    return false;
  }

  public int getIndex(int neuronNumber) {
    for (int i = 0; i < mask.size(); i++) {
      if (mask.get(i) == neuronNumber) {
        return i;
      }
    }
    throw new RuntimeException(
        String.format("Dendrite mask %s does not contain neuronNumber %d", this.toJson(), neuronNumber));
  }

  public void removeAndRemap(int neuronNumber) {
    for (int i = 0; i < mask.size(); i++) {
      if (mask.get(i) < neuronNumber) {
        continue;
      } else if (mask.get(i) == neuronNumber) {
        mask.remove(i);
        if (i < mask.size()) {
          mask.set(i, mask.get(i) - 1);
        }
      } else {
        mask.set(i, mask.get(i) - 1);
      }
    }
  }

  public static DendriteMask fromJson(Json json) {
    if (json == null) {
      return null;
    }
    return new DendriteMask(json.getJson("mask").asIntArray());
  }

  public Json toJson() {
    return Json.object().with("mask", Json.array(mask));
  }
}
