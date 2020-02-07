package ranger.server.api;

import java.io.File;
import java.util.Random;

import bowser.Controller;
import bowser.Handler;
import ox.IO;
import ox.Json;
import ranger.db.SessionDB;
import ranger.db.TrainingCheckpointHandleDB;
import ranger.db.model.Session;
import ranger.db.model.TrainingCheckpointHandle;
import ranger.nn.SingleLayerNeuralNetwork;
import ranger.nn.plot.NeuralFunctionPlot;
import ranger.server.service.NeuralNetworkService;

public class NeuralNetworkAPI extends Controller {

  private SessionDB sessionDB = new SessionDB();
  private TrainingCheckpointHandleDB trainingStepDB = new TrainingCheckpointHandleDB();

  private NeuralNetworkService neuralNetworkService = NeuralNetworkService.getInstance();

  @Override
  public void init() {
    route("POST", "/newNeuralNetwork").to(newNeuralNetwork);
    route("GET", "/trainingHistory").to(trainingHistory);
    route("POST", "/neuralFunctionPlot").to(neuralFunctionPlot);
  }

  private final Handler newNeuralNetwork = (request, response) -> {
    Json json = request.getJson();
    int hlSize = json.getInt("hiddenLayerSize");
    SingleLayerNeuralNetwork neuralNetwork = new SingleLayerNeuralNetwork(2, hlSize, 1).randomlyInitialize(new Random());
    response.write(Json.object().with("neuralNetwork", neuralNetwork.toJson()));
  };

  private final Handler trainingHistory = (request, response) -> {
    Json ret = IO.from(new File("history2.json")).toJson();
    response.write(ret);
  };

  private final Handler neuralFunctionPlot = (request, response) -> {
    Json json = request.getJson();
    Session session = sessionDB.get(json.getLong("sessionId"));
    TrainingCheckpointHandle trainingStep = trainingStepDB.getTrainingStep(session.id, json.getInt("trainingStep"));
    SingleLayerNeuralNetwork neuralNetwork = SingleLayerNeuralNetwork.fromJson(json.getJson("neuralNetwork"));
    NeuralFunctionPlot plot = NeuralFunctionPlot.plot(neuralNetwork);
    response.write(Json.object().with("plot", plot.toJson()));
  };
}
