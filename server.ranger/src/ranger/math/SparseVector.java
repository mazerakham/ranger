package ranger.math;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import ox.Json;

@SuppressWarnings("serial")
public class SparseVector extends HashMap<UUID, Double> {

  public SparseVector plus(SparseVector that) {
    SparseVector ret = new SparseVector();
    Set<UUID> allKeys = Sets.union(this.keySet(), that.keySet());
    for (UUID key : allKeys) {
      ret.put(key, this.getEntry(key) + that.getEntry(key));
    }
    return ret;
  }

  public double getEntry(UUID key) {
    if (containsKey(key)) {
      return get(key);
    } else {
      return 0.0;
    }
  }

  public double dot(SparseVector that) {
    double ret = 0.0;
    for (Entry<UUID, Double> entry : entrySet()) {
      if (that.containsKey(entry.getKey())) {
        ret += entry.getValue() * that.get(entry.getKey());
      }
    }
    return ret;
  }

  public SparseVector scale(double d) {
    SparseVector ret = new SparseVector();
    for (Entry<UUID, Double> entry : entrySet()) {
      ret.put(entry.getKey(), d * entry.getValue());
    }
    return ret;
  }

  public static SparseVector fromJson(Json json) {
    SparseVector ret = new SparseVector();
    for (String key : json.asJsonObject().keySet()) {
      ret.put(UUID.fromString(key), json.getDouble(key));
    }
    return ret;
  }

  public Json toJson() {
    Json ret = Json.object();
    for (Entry<UUID, Double> entry : entrySet()) {
      ret.with(entry.getKey().toString(), entry.getValue());
    }
    return ret;
  }
}
