package ranger.server;

import bowser.WebServer;
import ox.Log;
import ranger.db.RangerDB;
import ranger.server.api.NeuralNetworkAPI;
import ranger.server.api.SessionAPI;

public class RangerServer {

  private static final int PORT = 3001;

  public void start() {
    new WebServer("Ranger", PORT, false)
        .controller(new NeuralNetworkAPI())
        .controller(new SessionAPI())
        .start();

    Log.debug("Ranger Server started on port %d", PORT);
  }

  public static void main(String... args) {
    
    RangerDB.connectToDatabase("ranger");

    new RangerServer().start();
  }
}
