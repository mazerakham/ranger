package ranger.db.model;

import ox.Json;

public class Session extends AbstractModel {

  public long datasetHandleId;

  public Session(long datasetHandleId) {
    this.datasetHandleId = datasetHandleId;
  }

  public Json toJson() {
    return Json.object().with("datasetHandleId", datasetHandleId);
  }
}
