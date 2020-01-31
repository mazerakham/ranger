package ranger.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ranger.math.Vector;

public class Dataset {

  private static final double INPUT_NOISE = 0.5;

  private static final double OUTPUT_NOISE = 0.3;

  public List<LabeledDatapoint> labeledDatapoints = new ArrayList<>();

  public static Dataset generateBasicDataset(int numExamples, Random random) {
    Dataset ret = new Dataset();
    for (int i = 0; i < numExamples; i++) {
      ret.add(generateLabeledDataPoint(random));
    }
    return ret;
  }

  public static LabeledDatapoint generateLabeledDataPoint(Random random) {
    int i1 = random.nextDouble() > 0.5 ? 1 : 0;
    int i2 = random.nextDouble() > 0.5 ? 1 : 0;
    double x1 = i1 + INPUT_NOISE * random.nextGaussian();
    double x2 = i2 + INPUT_NOISE * random.nextGaussian();
    double y1 = ((i1 == 1 ^ i2 == 1) ? 1.0 : 0.0) + OUTPUT_NOISE * random.nextGaussian();

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
