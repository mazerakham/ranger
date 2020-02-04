package ranger.nn.train;

import java.util.ArrayList;
import java.util.List;

import ox.Json;

public class TrainingHistory {

  private List<TrainingEntry> entries = new ArrayList<>();

  public void addEntry(TrainingEntry entry) {
    entries.add(entry);
  }

  public Json toJson() {
    return Json.array(entries, TrainingEntry::toJson);
  }
}
