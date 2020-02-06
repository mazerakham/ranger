package ranger.db.model;

import ox.Json;

public class Session extends AbstractModel {

  public int datasetHandleId;

  public Session(int datasetHandleId) {
    this.datasetHandleId = datasetHandleId;
  }

  public Json toJson() {
    return Json.object().with("datasetHandleId", datasetHandleId);
  }
}
