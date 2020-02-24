package ranger.randomforest;

import static ox.util.Functions.map;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import ox.Json;
import ox.Log;
import ranger.data.sets.RegressionDataset;
import ranger.math.Vector;

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

  public RandomForestRegressor fit(RegressionDataset dataset) {
    DecisionTreeRegressionDataset preprocessedDataset = DecisionTreeRegressionDataset.fromDataset(dataset);
    BootstrapSampler sampler = new BootstrapSampler(preprocessedDataset, examplesPerTree);
    for (int i = 0; i < numTrees; i++) {
      DecisionTreeRegressionDataset sample = sampler.getSample(random);
      DecisionTree decisionTree = new DecisionTree(leafSize, maxDepth).fit(sample);
      trees.add(decisionTree);
    }
    return this;
  }

  public double predict(Vector v) {
    double total = 0.0;
    for (DecisionTree tree : trees) {
      total += tree.predict(v);
    }
    return total / trees.size();
  }

  public Json toJson() {
    return Json.array(map(trees, tree -> tree.toJson()));
  }

  public void prettyPrint() {
    Log.debug("Hello world here.");
    Log.debug(this.toJson().prettyPrint());
  }
}
