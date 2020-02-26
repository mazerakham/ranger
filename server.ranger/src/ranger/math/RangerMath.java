package ranger.math;

import static com.google.common.base.Preconditions.checkState;
import static ox.util.Functions.map;
import static ox.util.Functions.sum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RangerMath {

  /**
   * Return the list of ints [0 ... max).
   */
  public static List<Integer> range(int max) {
    ArrayList<Integer> ret = new ArrayList<>();
    for (int i = 0; i < max; i++) {
      ret.add(i);
    }
    return ret;
  }

  /**
   * Fisher-Yates shuffle, gets random set of N longs from large interval of size M for O(N) space and time.
   */
  public static Collection<Long> getRandomBagOfLongs(long min, long max, long numResults, Random r) {
    checkState(numResults <= max - min);
    Random random = (r == null) ? new Random() : r;
    long currRange = max - min;
    long currMin = min;
    long currNumResults = 0;
    HashMap<Long, Long> remapping = new HashMap<>();
    List<Long> ret = new ArrayList<>();
    while (currNumResults < numResults) {
      long randomChoice = ((long) (random.nextDouble() * currRange)) + currMin;
      ret.add(remapping.getOrDefault(randomChoice, randomChoice));
      remapping.put(randomChoice, remapping.getOrDefault(currMin, currMin));
      currRange--;
      currMin++;
      currNumResults++;
    }
    return ret;
  }

  /**
   * Fisher-Yates shuffle, gets random set of N ints from large interval of size M for O(N) space and time.
   */
  public static Collection<Integer> getRandomBagOfInts(int min, int max, int numResults, Random r) {
    return map(getRandomBagOfLongs(min, max, numResults, r), l -> l.intValue());
  }

  public static Vector gaussianVector(int dimension, double stdDev, Random random) {
    Vector ret = new Vector();
    for (int i = 0; i < dimension; i++) {
      ret.addEntry(random.nextGaussian());
    }
    return ret.scale(stdDev / Math.sqrt(dimension));
  }

  /**
   * Average of a list.
   */
  public static double average(Collection<Double> numbers) {
    return sum(numbers, n -> n) / numbers.size();
  }

  public static Vector relu(Vector vector) {
    Vector ret = new Vector();
    for (int i = 0; i < vector.size(); i++) {
      ret.addEntry(relu(vector.getEntry(i)));
    }
    return ret;
  }

  public static double relu(double d) {
    return (d < 0) ? 0 : d;
  }
  
  public static Vector heaviside(Vector vector) {
    Vector ret = new Vector();
    for (int i = 0; i < vector.size(); i++) {
      ret.addEntry(heaviside(vector.getEntry(i)));
    }
    return ret;
  }

  public static double heaviside(double d) {
    return d > 0.0 ? 1.0 : 0.0;
  }

  public static Vector signum(Vector vector) {
    Vector ret = new Vector();
    for (int i = 0; i < vector.size(); i++) {
      ret.addEntry(Math.signum(vector.getEntry(i)));
    }
    return ret;
  }

  public static Matrix signum(Matrix matrix) {
    return Matrix.fromFunction(matrix.height(), matrix.width(), (i, j) -> Math.signum(matrix.getEntry(i, j)));
  }
}
