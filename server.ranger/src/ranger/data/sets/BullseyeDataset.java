package ranger.data.sets;

import java.util.Random;

import ranger.math.RangerMath;

import ranger.data.LabeledDatapoint;
import ranger.math.Vector;
import ranger.nn.plot.Window;

public class BullseyeDataset {

  private static final double MAX_RADIUS = 1;
  private static final double INNER_BAND = 0.33;
  private static final double OUTER_BAND = 0.67;

  public static Dataset generateDefaultBullseyeDataset(int numExamples) {
    return generateBullseyeDataset(numExamples, 0.0, 0.0, new Random());
  }

  public static Dataset generateBullseyeDataset(int numExamples, double inputNoise, double outputNoise, Random random) {
    Dataset ret = new Dataset();
    for (int i = 0; i < numExamples; i++) {
      ret.add(generateDatapoint(inputNoise, outputNoise, random));
    }
    return ret;
  }

  public static LabeledDatapoint generateDatapoint(double inputNoise, double outputNoise, Random random) {
    double r = Math.sqrt(random.nextDouble());
    double theta = random.nextDouble() * 2 * Math.PI;
    Vector actualDatapoint = new Vector(r * Math.cos(theta), r * Math.sin(theta));
    Vector inputNoiseVector = RangerMath.gaussianVector(2, random).scale(inputNoise);
    double actualLabel = getOptimalValue(actualDatapoint);
    double outputNoiseSample = random.nextGaussian() * outputNoise;
    return new LabeledDatapoint(actualDatapoint.plus(inputNoiseVector), new Vector(actualLabel + outputNoiseSample));
  }

  public static double getOptimalValue(Vector in) {
    double r = in.magnitude();

    double distA = r;
    double distB = Math.abs(r - 0.5);
    double distC = Math.abs(r - 1);

    double eps = 0.01;
    double wA = 1.0 / Math.pow(distA + eps, 5);
    double wB = 1.0 / Math.pow(distB + eps, 5);
    double wC = 1.0 / Math.pow(distC + eps, 5);

    return wB / (wA + wB + wC);
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

  public static Window getWindow() {
    return new Window(-1.2, 1.2, -1.2, 1.2);
  }
}
