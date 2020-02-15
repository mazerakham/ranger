package ranger.nn.ranger;

import java.util.HashMap;
import java.util.UUID;

import ox.Json;

public class NeuronMap extends HashMap<UUID, Double> {

  public Json toJson() {
    Json ret = Json.object();
    for (Entry<UUID, Double> entry : entrySet()) {
      ret.with(entry.getKey().toString(), entry.getValue());
    }
    return ret;
  }

  public static NeuronMap fromJson(Json json) {
    if (json == null) {
      return null;
    }
    NeuronMap ret = new NeuronMap();
    for (String key : json.asJsonObject().keySet()) {
      ret.put(UUID.fromString(key), json.getDouble(key));
    }
    return ret;
  }
}
