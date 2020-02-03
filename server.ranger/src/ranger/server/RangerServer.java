package ranger.server;

import bowser.WebServer;
import ox.Log;

public class RangerServer {

  private static final int PORT = 3001;

  public void start() {
    new WebServer("Ranger", PORT, false)
        .controller(new NeuralNetworkAPI())
        .start();

    Log.debug("Ranger Server started on port %d", PORT);
  }

  public static void main(String... args) {
    new RangerServer().start();
  }
}
