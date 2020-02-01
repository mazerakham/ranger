package ranger.data;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import ranger.math.RangerMath;

public class TrainTestSplit {

  private final Dataset dataset;
  public final Dataset trainingSet = new Dataset();
  public final Dataset testSet = new Dataset();

  public TrainTestSplit(Dataset dataset) {
    this.dataset = dataset;
  }

  public TrainTestSplit split(double trainingRatio, Random r) {
    int trainingSetSize = (int) (trainingRatio * dataset.size());
    int testSetSize = dataset.size() - trainingSetSize;
    Collection<Integer> testIndices = RangerMath.getRandomBagOfInts(0, dataset.size(), testSetSize, r);
    Set<Integer> testIndicesSet = Sets.newConcurrentHashSet(testIndices);
    
    for (int i = 0; i < dataset.size(); i++) {
      if (testIndicesSet.contains(i)) {
        testSet.add(dataset.get(i));
      } else {
        trainingSet.add(dataset.get(i));
      }
    }
    
    return this;
  }


}
