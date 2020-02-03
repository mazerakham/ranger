package ranger.server;

import bowser.Controller;
import bowser.Handler;
import ox.Json;
import ranger.server.service.NeuralNetworkService;

public class NeuralNetworkAPI extends Controller {

  NeuralNetworkService neuralNetworkService = NeuralNetworkService.getInstance();

  @Override
  public void init() {
    route("POST", "/newNeuralNetwork").to(newNeuralNetwork);
  }

  private final Handler newNeuralNetwork = (request, response) -> {
    Json json = request.getJson();
    response.write(json.toString());
  };

}
