package ranger.nn.ranger;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import ranger.math.Vector;

public class SignalVector {

  public Map<UUID, Signal> signals = Maps.newHashMap();

  public int size() {
    return signals.size();
  }

  public Vector values() {
    return Vector.fromFunction(size(), i -> signals.get(i).value);
  }

  public void addSignal(UUID neuronUUID, Signal signal) {
    signals.merge(neuronUUID, signal, (oldSignal, newSignal) -> {
      throw new UnsupportedOperationException();
    });
  }

}
