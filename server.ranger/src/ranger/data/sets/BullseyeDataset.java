package ranger.data.sets;

import java.util.Random;

import ranger.math.RangerMath;

import ranger.data.LabeledDatapoint;
import ranger.math.Vector;

public class BullseyeDataset {

  private static final double MAX_RADIUS = 1;
  private static final double INNER_BAND = 0.33;
  private static final double OUTER_BAND = 0.67;

  public static Dataset generateBullseyeDataset(int numExamples, double inputNoise, double outputNoise, Random random) {
    Dataset ret = new Dataset();
    for (int i = 0; i < numExamples; i++) {
      ret.add(generateDatapoint(inputNoise, outputNoise, random));
    }
    return ret;
  }

  public static LabeledDatapoint generateDatapoint(double inputNoise, double outputNoise, Random random) {
    double r = random.nextDouble();
    double theta = random.nextDouble() * 2 * Math.PI;
    Vector actualDatapoint = new Vector(r * Math.cos(theta), r * Math.sin(theta));
    Vector inputNoiseVector = RangerMath.gaussianVector(2, inputNoise, random);
    double actualLabel = getLabel(r);
    double outputNoiseSample = random.nextGaussian() * outputNoise;
    return new LabeledDatapoint(actualDatapoint.plus(inputNoiseVector), new Vector(actualLabel + outputNoiseSample));
  }

  public static double getLabel(double r) {
    if (r < INNER_BAND) {
      return 0.0;
    } else if (r < OUTER_BAND) {
      return 1.0;
    } else {
      return 0.0;
    }
  }
}
