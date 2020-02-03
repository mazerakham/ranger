package ranger.server;

import java.util.Random;

import bowser.Controller;
import bowser.Handler;
import ox.Json;
import ranger.nn.NeuralNetwork;
import ranger.server.service.NeuralNetworkService;

public class NeuralNetworkAPI extends Controller {

  NeuralNetworkService neuralNetworkService = NeuralNetworkService.getInstance();

  @Override
  public void init() {
    route("POST", "/newNeuralNetwork").to(newNeuralNetwork);
  }

  private final Handler newNeuralNetwork = (request, response) -> {
    Json json = request.getJson();
    int hlSize = json.getInt("hiddenLayerSize");
    NeuralNetwork neuralNetwork = new NeuralNetwork(2, hlSize, 1).randomlyInitialize(new Random());
    response.write(Json.object().with("neuralNetwork", neuralNetwork.toJson()));
  };

}
