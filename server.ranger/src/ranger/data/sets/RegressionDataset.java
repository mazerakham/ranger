package ranger.data.sets;

import static ox.util.Functions.map;

import java.util.ArrayList;
import java.util.Collection;

import ranger.data.LabeledRegressionDatapoint;

@SuppressWarnings("serial")
public class RegressionDataset extends ArrayList<LabeledRegressionDatapoint> {

  public RegressionDataset() {
    super();
  }

  public RegressionDataset(Collection<LabeledRegressionDatapoint> labeledDatapoints) {
    super();
    this.addAll(labeledDatapoints);
  }

  public static RegressionDataset fromDataset(Dataset dataset) {
    return new RegressionDataset(
        map(dataset.labeledDatapoints, ldp -> new LabeledRegressionDatapoint(ldp.datapoint, ldp.label.toScalar())));
  }

}
