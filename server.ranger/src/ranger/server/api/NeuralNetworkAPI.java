package ranger.server.api;

import static com.google.common.base.Preconditions.checkState;
import static ox.util.Utils.parseEnum;

import java.util.List;
import java.util.Random;

import bowser.Controller;
import bowser.Handler;
import ox.Json;
import ox.Log;
import ranger.arch.PlainNeuralNetworkSpecs;
import ranger.data.Batcher;
import ranger.data.sets.Dataset;
import ranger.data.sets.Dataset.DatasetType;
import ranger.nn.PlainNeuralNetwork;
import ranger.nn.plot.NeuralFunctionPlot;
import ranger.nn.train.SGDTrainer;
import ranger.server.service.NeuralNetworkService;

public class NeuralNetworkAPI extends Controller {

  NeuralNetworkService neuralNetworkService = NeuralNetworkService.getInstance();

  @Override
  public void init() {
    route("POST", "/newNeuralNetwork").to(newNeuralNetwork);
    route("POST", "/train").to(train);
    route("POST", "/neuralFunctionPlot").to(neuralFunctionPlot);
    route("POST", "/desiredPlot").to(desiredPlot);
  }

  private final Handler newNeuralNetwork = (request, response) -> {
    Json json = request.getJson();
    Json nnJson = json.getJson("neuralNetworkSpecs");
    PlainNeuralNetworkSpecs specs = parsePlainNeuralNetworkSpecs(nnJson);
    PlainNeuralNetwork neuralNetwork = new PlainNeuralNetwork(specs).initialize(new Random());
    response.write(Json.object().with("neuralNetwork", neuralNetwork.toJson()));
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

  private final Handler neuralFunctionPlot = (request, response) -> {
    Json json = request.getJson();
    checkState(json.hasKey("neuralNetwork"), "Request to /neuralFunctionPlot requires a neuralNetwork parameter.");
    PlainNeuralNetwork neuralNetwork = PlainNeuralNetwork.fromJson(json.getJson("neuralNetwork"));
    checkState(json.hasKey("datasetType"), "Request to /neuralFunctionPlot requires a datasetType parametere.");
    DatasetType datasetType = parseEnum(json.get("datasetType").toUpperCase(), DatasetType.class);

    NeuralFunctionPlot plot = NeuralFunctionPlot.plot(neuralNetwork, datasetType);
    response.write(Json.object().with("plot", plot.toJson()));
  };

  private final Handler desiredPlot = (request, response) -> {
    // TODO: Right now we just send back X-OR.
    Json json = request.getJson();
    checkState(json.hasKey("datasetType"), "Request to /desiredPlot requires a datasetType parameter");
    
    DatasetType type = parseEnum(json.get("datasetType").toUpperCase(), DatasetType.class);
    if (type == DatasetType.XOR) {
      response.write(Json.object().with("plot", NeuralFunctionPlot.xOrDesiredPlot().toJson()));
    } else {
      checkState(type == DatasetType.BULLSEYE);
      NeuralFunctionPlot plot = NeuralFunctionPlot.bullseyeDesiredPlot();
      Log.debug(plot);
      response.write(Json.object().with("plot", plot.toJson()));
    }
  };

  private static PlainNeuralNetworkSpecs parsePlainNeuralNetworkSpecs(Json nnJson) {
    int inputLayerSize = 2;
    int outputLayerSize = 1;
    List<Integer> layerSizes = nnJson.getJson("hiddenLayerSizes").asIntArray();
    checkState(nnJson.getInt("numHiddenLayers") == layerSizes.size());
    layerSizes.add(0, inputLayerSize);
    layerSizes.add(outputLayerSize);
    return new PlainNeuralNetworkSpecs(layerSizes.size(), layerSizes);
  }


}
