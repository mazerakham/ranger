package ranger.randomforest;

import java.util.Map;

import com.google.common.collect.Maps;

import ranger.data.LabeledRegressionDatapoint;
import ranger.data.sets.RegressionDataset;
import ranger.math.Vector;

public class FastSplitter {

  public int numFeatures;

  private Map<Integer, FeatureStatistics> featureStatistics = Maps.newHashMap();

  public FastSplitter computeStatistics(RegressionDataset dataset) {
    LabeledRegressionDatapoint example = dataset.get(0);
    Vector datapoint = example.datapoint;
    numFeatures = datapoint.size();
    for (int i = 0; i < numFeatures; i++) {
      featureStatistics.put(i, computeStatistics(dataset, i));
    }
    return this;
  }

  private FeatureStatistics computeStatistics(RegressionDataset dataset, int featureIndex) {
    boolean isOneHot = true;
    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;
    for (int i = 0; i < dataset.size(); i++) {
      Vector datapoint = dataset.get(i).datapoint;
      double val = datapoint.getEntry(featureIndex);
      if (val != 0.0 && val != 1.0) {
        isOneHot = false;
      }
      min = Math.min(min, val);
      max = Math.max(max, val);
    }
    return new FeatureStatistics(min, max, isOneHot);
  }

  public Split computeOptimalSplit(RegressionDataset dataset, int leafSize) {
    Split ret = null;
    for (int i = 0; i < numFeatures; i++) {
      Split candidate = computeOptimalSplit(dataset, leafSize, i);
      if (candidate.isBetterThan(ret)) {
        ret = candidate;
      }
    }
    return ret;
  }

  private Split computeOptimalSplit(RegressionDataset dataset, int leafSize, int featureIndex) {
    FeatureStatistics statistics = featureStatistics.get(featureIndex);
  }

}
