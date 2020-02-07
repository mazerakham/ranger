package ranger.server.api;

import static com.google.common.base.Preconditions.checkState;

import java.util.Random;

import bowser.Controller;
import bowser.Handler;
import ox.Json;
import ox.Log;
import ranger.arch.PlainNeuralNetworkSpecs;
import ranger.arch.SessionOptions;
import ranger.data.sets.Dataset;
import ranger.data.sets.Dataset.DatasetType;
import ranger.db.DatasetHandleDB;
import ranger.db.SessionDB;
import ranger.db.TrainingCheckpointHandleDB;
import ranger.db.model.DatasetHandle;
import ranger.db.model.Session;
import ranger.db.model.TrainingCheckpointHandle;
import ranger.nn.PlainNeuralNetwork;
import ranger.nn.train.TrainingCheckpoint;

public class SessionAPI extends Controller {

  private final SessionDB sessionDB = new SessionDB();
  private final DatasetHandleDB datasetHandleDB = new DatasetHandleDB();
  private final TrainingCheckpointHandleDB trainingCheckpointHandleDB = new TrainingCheckpointHandleDB();

  @Override
  public void init() {
    route("POST", "/createSession").to(createSession);
  }

  private final Handler createSession = (request, response) -> {
    // Parse request.
    Json json = request.getJson();
    Log.debug(json.getJson("sessionOptions"));
    SessionOptions sessionOptions = SessionOptions.fromJson(json.getJson("sessionOptions"));
    Log.debug(sessionOptions);

    // Make dataset.
    DatasetType datasetType = sessionOptions.datasetType;
    Dataset dataset = Dataset.generateDataset(datasetType);
    DatasetHandle datasetHandle = DatasetHandle.createDatasetHandle(dataset);
    datasetHandleDB.insert(datasetHandle);

    // Make session.
    Session session = new Session(datasetHandle.id);
    sessionDB.insert(session);

    // Make randomly initialized neural network as training step 0.
    checkState(json.getJson("sessionOptions").get("modelType").equals("plain"),
        "Ranger API only supports plain neural network sessions for now.");
    PlainNeuralNetworkSpecs specs = sessionOptions.neuralNetworkSpecs;
    PlainNeuralNetwork neuralNetwork = new PlainNeuralNetwork(specs).initialize(new Random());

    // Make first training step for the session.
    TrainingCheckpoint checkpoint = new TrainingCheckpoint(session.id, 0L, 0L);
    TrainingCheckpointHandle checkpointHandle = TrainingCheckpointHandle.createHandle(checkpoint);
    trainingCheckpointHandleDB.insert(checkpointHandle);

    // Send session (for ID) and neural network.
    response.write(Json.object()
        .with("session", session.toJson())
        .with("neuralNetwork", neuralNetwork.toJson()));
  };

}
