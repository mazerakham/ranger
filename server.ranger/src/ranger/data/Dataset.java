package ranger.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ranger.math.Vector;

public class Dataset {

  public List<LabeledDatapoint> labeledDatapoints = new ArrayList<>();

  public static Dataset generateBasicDataset(int numExamples, double inputNoise, double outputNoise, Random random) {
    Dataset ret = new Dataset();
    for (int i = 0; i < numExamples; i++) {
      ret.add(generateLabeledDataPoint(inputNoise, outputNoise, random));
    }
    return ret;
  }

  public static LabeledDatapoint generateLabeledDataPoint(double inputNoise, double outputNoise, Random random) {
    int i1 = random.nextDouble() > 0.5 ? 1 : 0;
    int i2 = random.nextDouble() > 0.5 ? 1 : 0;
    double x1 = i1 + inputNoise * random.nextGaussian();
    double x2 = i2 + inputNoise * random.nextGaussian();
    double y1 = ((i1 == 1 ^ i2 == 1) ? 1.0 : 0.0) + outputNoise * random.nextGaussian();

    return new LabeledDatapoint(new Vector(x1, x2), new Vector(y1));
  }

  public void add(LabeledDatapoint datapoint) {
    labeledDatapoints.add(datapoint);
  }

  public LabeledDatapoint get(int i) {
    return labeledDatapoints.get(i);
  }

  public int size() {
    return labeledDatapoints.size();
  }

}
