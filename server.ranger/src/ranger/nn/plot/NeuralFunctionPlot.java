package ranger.nn.plot;

import ox.Json;
import ranger.math.Vector;
import ranger.nn.NeuralNetwork;

public class NeuralFunctionPlot {

  private static final int RESOLUTION = 100;

  private final Window window = new Window(-1, 1, -1, 1);
  private double[][] plot = new double[RESOLUTION][RESOLUTION];

  public static NeuralFunctionPlot plot(NeuralNetwork neuralNetwork) {
    NeuralFunctionPlot ret = new NeuralFunctionPlot();
    for (int i = 0; i < RESOLUTION; i++) {
      for (int j = 0; j < RESOLUTION; j++) {
        Vector in = ret.window.getPoint(i, j, RESOLUTION);
        ret.plot[i][j] = in.toScalar();
      }
    }
    return ret;
  }

  public Json toJson() {
    return Json.array(plot, doubleArray -> Json.array(doubleArray));
  }

  private static class Window {
    private double xMin, xMax, yMin, yMax;

    public Window(double xMin, double xMax, double yMin, double yMax) {
      this.xMin = xMin;
      this.xMax = xMax;
      this.yMin = yMin;
      this.yMax = yMax;
    }

    public Vector getPoint(int i, int j, int resolution) {
      return new Vector(
          xMin + (xMax - xMin) * i / resolution,
          yMin + (yMax - yMin) * i / resolution
          );
    }
  }
}

