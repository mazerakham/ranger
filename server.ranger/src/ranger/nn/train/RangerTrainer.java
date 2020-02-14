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

  public void performTrainingStep(Random random) {
    LabeledDatapoint labeledDatapoint = Dataset.generateDataset(datasetType, 1).get(0);
    Vector datapoint = labeledDatapoint.datapoint;
    Vector label = labeledDatapoint.label;

    Log.debug(rangerNetwork.toJson().prettyPrint());
    rangerNetwork.expand();
    rangerNetwork.growNewNeurons(random);
    rangerNetwork.propagateForward(datapoint);
    rangerNetwork.propagateBackward(label);
    rangerNetwork.updateNeurons();
    rangerNetwork.contract();
    Log.debug("\n\nAfter training:\n\n");
    Log.debug(rangerNetwork.toJson().prettyPrint());
  }
}
