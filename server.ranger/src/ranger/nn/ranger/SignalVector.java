package ranger.nn.ranger;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

public class SignalVector {

  public Map<UUID, Signal> signals = Maps.newHashMap();

  public int size() {
    return signals.size();
  }

  public void addSignal(UUID neuronUUID, Signal signal) {
    signals.merge(neuronUUID, signal, (oldSignal, newSignal) -> {
      throw new UnsupportedOperationException();
    });
  }

}
