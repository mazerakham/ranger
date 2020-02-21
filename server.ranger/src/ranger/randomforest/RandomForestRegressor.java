package ranger.randomforest;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import ranger.data.sets.Dataset;

public class RandomForestRegressor {

  private final Random random;

  public final int numTrees;

  public final int examplesPerTree;

  public final int leafSize;

  public final int maxDepth;

  private final List<DecisionTree> trees;

  private BootstrapSampler sampler;

  public RandomForestRegressor(int numTrees, int examplesPerTree, int leafSize, int maxDepth, Random random) {
    this.numTrees = numTrees;
    this.examplesPerTree = examplesPerTree;
    this.leafSize = leafSize;
    this.maxDepth = maxDepth;
    this.random = random;
    this.trees = Lists.newArrayListWithCapacity(numTrees);
  }

  public RandomForestRegressor fit(Dataset dataset) {
    BootstrapSampler sampler = new BootstrapSampler(dataset, examplesPerTree);
    for (int i = 0; i < numTrees; i++) {
      Dataset sample = sampler.getSample(random);
      DecisionTree decisionTree = new DecisionTree(leafSize, maxDepth).fit(sample);
    }
    return this;
  }

  public void prettyPrint() {
    throw new UnsupportedOperationException();
  }
}
