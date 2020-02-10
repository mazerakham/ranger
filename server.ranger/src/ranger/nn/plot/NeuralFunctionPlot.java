package ranger.nn.plot;

import static com.google.common.base.Preconditions.checkState;

import java.util.function.Function;

import ox.Json;
import ranger.data.sets.BullseyeDataset;
import ranger.data.sets.Dataset.DatasetType;
import ranger.data.sets.XOrDataset;
import ranger.math.Vector;

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

  public static NeuralFunctionPlot plot(Function<Vector, Vector> model, DatasetType datasetType) {
    if (datasetType == DatasetType.XOR) {
      return plot(model, XOrDataset.getWindow());
    } else {
      checkState(datasetType == DatasetType.BULLSEYE);
      return plot(model, BullseyeDataset.getWindow());
    }
  }

  public static NeuralFunctionPlot plot(Function<Vector, Vector> model, Window window) {
    NeuralFunctionPlot ret = new NeuralFunctionPlot(window);
    for (int i = 0; i < RESOLUTION; i++) {
      for (int j = 0; j < RESOLUTION; j++) {
        Vector in = ret.window.getPoint(i, j, RESOLUTION);
        ret.plot[i][j] = model.apply(in).toScalar();
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

