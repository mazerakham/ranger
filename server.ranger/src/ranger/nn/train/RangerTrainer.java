package ranger.nn.train;

import java.util.Random;

import ox.Log;
import ranger.data.LabeledDatapoint;
import ranger.data.sets.Dataset;
import ranger.data.sets.Dataset.DatasetType;
import ranger.math.Vector;
import ranger.nn.ranger.RangerNetwork;

public class RangerTrainer {

  private final RangerNetwork rangerNetwork;
  private final DatasetType datasetType;

  public RangerTrainer(RangerNetwork rangerNetwork, DatasetType datasetType) {
    this.rangerNetwork = rangerNetwork;
    this.datasetType = datasetType;
  }

  public void growNewNeuron(Random random, int layer, boolean startNewLayer) {
    throw new UnsupportedOperationException();
  }

  public void performTrainingStep(Random random) {
    LabeledDatapoint labeledDatapoint = Dataset.generateDataset(datasetType, 1).get(0);
    Vector datapoint = labeledDatapoint.datapoint;
    Vector label = labeledDatapoint.label;

    Log.debug(rangerNetwork.toJson().prettyPrint());
    try {
      rangerNetwork.propagateForward(datapoint);
      rangerNetwork.propagateBackward(label);
    } catch (Exception e) {
      Log.debug("The error happened with ranger network in this state:");
      Log.debug(rangerNetwork.toJson().prettyPrint());
      throw e;
    }
    Log.debug("\n\nAfter training:\n\n");
    Log.debug(rangerNetwork.toJson().prettyPrint());
  }
}
