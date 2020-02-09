package ranger.nn.plot;

import static com.google.common.base.Preconditions.checkState;

import ox.Json;
import ranger.data.sets.BullseyeDataset;
import ranger.data.sets.Dataset.DatasetType;
import ranger.data.sets.XOrDataset;
import ranger.math.Vector;
import ranger.nn.PlainNeuralNetwork;

public class NeuralFunctionPlot {

  private static final int RESOLUTION = 50;

  private final Window window;
  private double[][] plot = new double[RESOLUTION][RESOLUTION];

  public NeuralFunctionPlot(Window window) {
    this.window = window;
  }

  public NeuralFunctionPlot(double xMin, double xMax, double yMin, double yMax) {
    this(new Window(xMin, xMax, yMin, yMax));
  }

  public static NeuralFunctionPlot plot(PlainNeuralNetwork neuralNetwork, DatasetType datasetType) {
    if (datasetType == DatasetType.XOR) {
      return plot(neuralNetwork, XOrDataset.getWindow());
    } else {
      checkState(datasetType == DatasetType.BULLSEYE);
      return plot(neuralNetwork, BullseyeDataset.getWindow());
    }
  }

  public static NeuralFunctionPlot plot(PlainNeuralNetwork neuralNetwork, Window window) {
    NeuralFunctionPlot ret = new NeuralFunctionPlot(window);
    for (int i = 0; i < RESOLUTION; i++) {
      for (int j = 0; j < RESOLUTION; j++) {
        Vector in = ret.window.getPoint(i, j, RESOLUTION);
        ret.plot[i][j] = neuralNetwork.estimate(in).toScalar();
      }
    }
    return ret;
  }

  public static NeuralFunctionPlot xOrDesiredPlot() {
    NeuralFunctionPlot ret = new NeuralFunctionPlot(XOrDataset.getWindow());
    for (int i = 0; i < RESOLUTION; i++) {
      for (int j = 0; j < RESOLUTION; j++) {
        Vector in = ret.window.getPoint(i, j, RESOLUTION);
        ret.plot[i][j] = XOrDataset.getOptimalValue(in);
      }
    }
    return ret;
  }

  public static NeuralFunctionPlot bullseyeDesiredPlot() {
    NeuralFunctionPlot ret = new NeuralFunctionPlot(BullseyeDataset.getWindow());
    for (int i = 0; i < RESOLUTION; i++) {
      for (int j = 0; j < RESOLUTION; j++) {
        Vector in = ret.window.getPoint(i, j, RESOLUTION);
        ret.plot[i][j] = BullseyeDataset.getOptimalValue(in);
      }
    }
    return ret;
  }

  public Json toJson() {
    return Json.array(plot, doubleArray -> Json.array(doubleArray));
  }


}

