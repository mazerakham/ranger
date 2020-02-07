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
    Json json = request.getJson();
    Log.debug(json);
    DatasetType datasetType = parseEnum(json.get("datasetType").toUpperCase(), DatasetType.class);
    int batchSize = json.getInt("batchSize");
    Batcher batcher = new Batcher(Dataset.generateDataset(datasetType), batchSize);
    PlainNeuralNetwork neuralNetwork = PlainNeuralNetwork.fromJson(json.getJson("neuralNetwork"));
    SGDTrainer trainer = new SGDTrainer(batcher, 0.05, 1);
    trainer.train(neuralNetwork);
  };

  private final Handler neuralFunctionPlot = (request, response) -> {
    Json json = request.getJson();
    PlainNeuralNetwork neuralNetwork = PlainNeuralNetwork.fromJson(json.getJson("neuralNetwork"));
    NeuralFunctionPlot plot = NeuralFunctionPlot.plot(neuralNetwork);
    response.write(Json.object().with("plot", plot.toJson()));
  };

  private final Handler desiredPlot = (request, response) -> {
    // TODO: Right now we just send back X-OR.
    NeuralFunctionPlot plot = NeuralFunctionPlot.xOrDesiredPlot();
    response.write(Json.object().with("plot", plot.toJson()));
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
