package ranger.server.api;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import bowser.Controller;
import bowser.Handler;
import ox.Json;
import ox.Log;
import ranger.data.sets.Dataset;
import ranger.db.DatasetHandleDB;
import ranger.db.SessionDB;
import ranger.db.model.DatasetHandle;
import ranger.db.model.Session;

public class SessionAPI extends Controller {

  private final SessionDB sessionDB = new SessionDB();
  private final DatasetHandleDB datasetHandleDB = new DatasetHandleDB();

  @Override
  public void init() {
    route("POST", "/createSession").to(createSession);
  }

  private final Handler createSession = (request, response) -> {
    Json json = request.getJson();
    Log.debug(json);

    Dataset dataset = Dataset.generateDataset(json.getEnum("datasetType", Dataset.DatasetType.class));
    DatasetHandle datasetHandle = DatasetHandle.createDatasetHandle(dataset);
    datasetHandleDB.insert(datasetHandle);

    Session session = new Session(datasetHandle.id);
    sessionDB.insert(session);

    int numLayers = json.getInt("numLayers");
    List<Integer> layerSizes = json.getJson("layerSizes").asIntArray();
    checkState(numLayers == layerSizes.size());

    response.write(Json.object().with("session", session.toJson()));
  };

}
