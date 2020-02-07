package ranger.nn.plot;

import ox.Json;
import ranger.math.Vector;
import ranger.nn.PlainNeuralNetwork;

public class NeuralFunctionPlot {

  private static final int RESOLUTION = 50;

  private final Window window = new Window(0, 1, 0, 1);
  private double[][] plot = new double[RESOLUTION][RESOLUTION];

  // TODO this is not dry. Need to abstractify neural networks. But also, this only works for 2D input, so really this
  // is a temporary function anyway.
  public static NeuralFunctionPlot plot(PlainNeuralNetwork neuralNetwork) {
    NeuralFunctionPlot ret = new NeuralFunctionPlot();
    for (int i = 0; i < RESOLUTION; i++) {
      for (int j = 0; j < RESOLUTION; j++) {
        Vector in = ret.window.getPoint(i, j, RESOLUTION);
        ret.plot[i][j] = neuralNetwork.estimate(in).toScalar();
      }
    }
    return ret;
  }

  public static NeuralFunctionPlot xOrDesiredPlot() {
    NeuralFunctionPlot ret = new NeuralFunctionPlot();
    for (int i = 0; i < RESOLUTION; i++) {
      for (int j = 0; j < RESOLUTION; j++) {
        Vector in = ret.window.getPoint(i, j, RESOLUTION);
        ret.plot[i][j] = Math.abs(in.getEntry(0) - in.getEntry(1));
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
          yMin + (yMax - yMin) * j / resolution
          );
    }
  }
}

