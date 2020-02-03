package ranger.server.service;

public class NeuralNetworkService {

  private static NeuralNetworkService instance = new NeuralNetworkService();

  private NeuralNetworkService() {
    // singleton pattern.
  }

  public static NeuralNetworkService getInstance() {
    return instance;
  }
}
