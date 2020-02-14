package ranger.server.api;

import static com.google.common.base.Preconditions.checkState;
import static ox.util.Utils.parseEnum;

import java.util.Random;

import bowser.Controller;
import bowser.Handler;
import ox.Json;
import ox.Log;
import ranger.arch.PlainNeuralNetworkSpecs;
import ranger.data.Batcher;
import ranger.data.sets.Dataset;
import ranger.data.sets.Dataset.DatasetType;
import ranger.nn.plain.PlainNeuralNetwork;
import ranger.nn.plot.NeuralFunctionPlot;
import ranger.nn.ranger.RangerNetwork;
import ranger.nn.train.RangerTrainer;
import ranger.nn.train.SGDTrainer;
import ranger.server.service.NeuralNetworkService;

public class NeuralNetworkAPI extends Controller {

  private static final int DEFAULT_SEED = new Random().nextInt();
  private Random random = new Random(DEFAULT_SEED);

  private NeuralNetworkService neuralNetworkService = NeuralNetworkService.getInstance();

  @Override
  public void init() {
    route("POST", "/newNeuralNetwork").to(newNeuralNetwork);
    route("POST", "/train").to(train);
    route("POST", "/trainRanger").to(trainRanger);
    route("POST", "/neuralFunctionPlot").to(neuralFunctionPlot);
    route("POST", "/desiredPlot").to(desiredPlot);
  }

  private final Handler newNeuralNetwork = (request, response) -> {
    Json json = request.getJson();
    checkState(json.hasKey("modelType"), "Request to /newNeuralNetwork must specify modelType");
    checkState(json.hasKey("neuralNetworkSpecs"), "Request to /newNeuralNetwork must specify neuralNetworkSpecs");
    String modelType = json.get("modelType");
    Json nnSpecsJson = json.getJson("neuralNetworkSpecs");

    if (modelType.equals("plain")) {
      PlainNeuralNetworkSpecs specs = PlainNeuralNetworkSpecs.fromJson(nnSpecsJson);
      PlainNeuralNetwork neuralNetwork = new PlainNeuralNetwork(specs).initialize(new Random());
      response.write(Json.object().with("neuralNetwork", neuralNetwork.toJson()));
    } else if (modelType.equals("ranger")) {
      RangerNetwork rangerNetwork = new RangerNetwork(2, 1).initialize(random);
      response.write(Json.object().with("neuralNetwork", rangerNetwork.toJson()));
    } else {
      throw new RuntimeException("Could not recognize modelType: " + modelType);
    }
  };

  private final Handler train = (request, response) -> {
    // Parse JSON request.
    Json json = request.getJson();
    Log.debug(json);
    checkState(json.hasKey("datasetType"), "Request to /train must specify datasetType");
    DatasetType datasetType = parseEnum(json.get("datasetType").toUpperCase(), DatasetType.class);
    checkState(json.hasKey("batchSize"), "Request to /train must specify batchSize");
    int batchSize = json.getInt("batchSize");
    checkState(json.hasKey("numSteps"), "Request to /train must specify numSteps");
    int numSteps = json.getInt("numSteps");
    checkState(json.hasKey("learningRate"), "Request to /train must specify learningRate");
    double learningRate = json.getDouble("learningRate");

    Batcher batcher = new Batcher(Dataset.generateDataset(datasetType, batchSize * numSteps), batchSize);
    PlainNeuralNetwork neuralNetwork = PlainNeuralNetwork.fromJson(json.getJson("neuralNetwork"));
    SGDTrainer trainer = new SGDTrainer(batcher, learningRate, numSteps);
    trainer.train(neuralNetwork);
    response.write(Json.object()
        .with("neuralNetwork", neuralNetwork.toJson())
        .with("plot", NeuralFunctionPlot.plot(neuralNetwork, datasetType).toJson()));
  };

  private final Handler trainRanger = (request, response) -> {
    Json json = request.getJson();
    Log.debug("\ntrainRanger Handler received JSON:");
    Log.debug(json);
    checkState(json.hasKey("neuralNetwork"));
    checkState(json.hasKey("datasetType"));
    RangerNetwork rangerNetwork = RangerNetwork.fromJson(json.getJson("neuralNetwork"));
    DatasetType datasetType = parseEnum(json.get("datasetType").toUpperCase(), DatasetType.class);

    Log.debug("\nrangerNetwork before the 'training step':");
    Log.debug(rangerNetwork.toJson().prettyPrint());

    new RangerTrainer(rangerNetwork, datasetType).performTrainingStep(random);

    Log.debug("\nrangerNetwork at the end of the 'training step':");
    Log.debug(rangerNetwork.toJson().prettyPrint());

    response.write(Json.object()
        .with("neuralNetwork", rangerNetwork.toJson())
        .with("plot", NeuralFunctionPlot.plot(rangerNetwork, datasetType).toJson()));
  };

  private final Handler neuralFunctionPlot = (request, response) -> {
    Json json = request.getJson();
    checkState(json.hasKey("modelType"), "Request to /neuralFunctionPlot requires a modelType parameter.");
    checkState(json.hasKey("datasetType"), "Request to /neuralFunctionPlot requires a datasetType parametere.");
    checkState(json.hasKey("neuralNetwork"), "Request to /neuralFunctionPlot requires a neuralNetwork parameter.");

    String modelType = json.get("modelType");
    DatasetType datasetType = parseEnum(json.get("datasetType").toUpperCase(), DatasetType.class);
    if (modelType.equals("plain")) {
      PlainNeuralNetwork neuralNetwork = PlainNeuralNetwork.fromJson(json.getJson("neuralNetwork"));
      NeuralFunctionPlot plot = NeuralFunctionPlot.plot(neuralNetwork, datasetType);
      response.write(Json.object().with("plot", plot.toJson()));
    } else if (modelType.equals("ranger")) {
      RangerNetwork rangerNetwork = RangerNetwork.fromJson(json.getJson("neuralNetwork"));
      NeuralFunctionPlot plot = NeuralFunctionPlot.plot(rangerNetwork, datasetType);
      response.write(Json.object().with("plot", plot.toJson()));
    } else {
      throw new RuntimeException("modelType " + modelType + " is not supported.");
    }
  };

  private final Handler desiredPlot = (request, response) -> {
    Json json = request.getJson();
    checkState(json.hasKey("datasetType"), "Request to /desiredPlot requires a datasetType parameter");
    
    DatasetType type = parseEnum(json.get("datasetType").toUpperCase(), DatasetType.class);
    if (type == DatasetType.XOR) {
      response.write(Json.object().with("plot", NeuralFunctionPlot.xOrDesiredPlot().toJson()));
    } else if (type == DatasetType.BULLSEYE) {
      response.write(Json.object().with("plot", NeuralFunctionPlot.bullseyeDesiredPlot().toJson()));
    } else {
      throw new RuntimeException("datasetType " + type + " is not supported.");
    }
  };

}
