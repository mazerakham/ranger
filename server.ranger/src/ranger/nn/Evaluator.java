package ranger.nn;

import java.util.ArrayList;
import java.util.List;

import ranger.math.RangerMath;

import ox.Log;
import ranger.data.sets.Dataset;
import ranger.math.Vector;

public class Evaluator {

  public void evaluate(SingleLayerNeuralNetwork neuralNetwork, Dataset testDataset) {
    List<Double> errors = new ArrayList<>();
    for (int i = 0; i < testDataset.size(); i++) {
      Vector prediction = neuralNetwork.estimate(testDataset.get(i).datapoint);
      errors.add(prediction.distance(testDataset.get(i).label));
    }
    double averageError = RangerMath.average(errors);
    Log.debug("Typical errors:");
    for (int i = 0; i < 5; i++) {
      Log.debug(errors.get(i));
    }
    Log.debug("Average error:");
    Log.debug(averageError);
  }
}
