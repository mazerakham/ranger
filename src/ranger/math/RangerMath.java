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

  /**
   * Average of a list.
   */
  public static double average(Collection<Double> numbers) {
    return sum(numbers, n -> n) / numbers.size();
  }

  /**
   * x < 0 => 0
   * 
   * x > 0 => x.
   */
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
}
