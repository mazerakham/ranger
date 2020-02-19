package ranger.nn.ranger;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Maps;

import ox.Json;

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

  public Json toJson() {
    Json ret = Json.object();
    for (Entry<UUID, Signal> entry : signals.entrySet()) {
      ret.with(entry.getKey().toString(), entry.getValue().toJson());
    }
    return ret;
  }

}
