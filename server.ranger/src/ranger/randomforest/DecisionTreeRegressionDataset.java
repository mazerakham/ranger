package ranger.randomforest;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;

import ranger.data.LabeledRegressionDatapoint;
import ranger.data.sets.RegressionDataset;

@SuppressWarnings("serial")
public class DecisionTreeRegressionDataset extends ArrayList<LabeledRegressionDatapoint> {

  public int numFeatures;

  public static DecisionTreeRegressionDataset fromDataset(RegressionDataset dataset) {
    checkState(dataset.size() > 0, "DecisionTreeRegressionDataset cannot be empty.");
    DecisionTreeRegressionDataset ret = new DecisionTreeRegressionDataset();
    ret.addAll(dataset);
    ret.preprocess();
    return ret;
  }

  public DecisionTreeRegressionDataset getLeft(Split split) {
    throw new UnsupportedOperationException();
    // Copy the metadata, filter the rows.
  }

  public DecisionTreeRegressionDataset getRight(Split split) {
    throw new UnsupportedOperationException();
    // Copy the metadata, filter the rows.
  }

  public void preprocess() {
    numFeatures = get(0).datapoint.size();
    throw new UnsupportedOperationException();
  }

  @Override
  public DecisionTreeRegressionDataset clone() {
    DecisionTreeRegressionDataset ret = new DecisionTreeRegressionDataset();
    ret.addAll(this);
    // Still need to copy the metadata.
    throw new UnsupportedOperationException();
  }
}
