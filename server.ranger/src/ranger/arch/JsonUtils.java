package ranger.arch;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import ox.Json;

public class JsonUtils {

  public static Json neuronIndexToJson(Map<Integer, UUID> neuronIndex) {
    if (neuronIndex == null) {
      return null;
    }
    Json ret = Json.object();
    for (Entry<Integer, UUID> entry : neuronIndex.entrySet()) {
      ret.with(entry.getKey().toString(), entry.getValue().toString());
    }
    return ret;
  }

  public static BiMap<Integer, UUID> neuronIndexFromJson(Json json) {
    if (json == null) {
      return null;
    }
    BiMap<Integer, UUID> ret = HashBiMap.create();
    for (String key : json.asJsonObject().keySet()) {
      ret.put(Integer.parseInt(key), UUID.fromString(json.get(key)));
    }
    return ret;
  }
}
