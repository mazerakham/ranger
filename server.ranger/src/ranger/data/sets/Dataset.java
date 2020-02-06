package ranger.data.sets;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.List;

import ox.Json;
import ranger.data.LabeledDatapoint;

public class Dataset {

  public List<LabeledDatapoint> labeledDatapoints = new ArrayList<>();

  public static Dataset generateDataset(DatasetType type) {
    if (type == DatasetType.XOR) {
      return XOrDataset.generateDefaultXOrDataset();
    } else {
      checkState(type == DatasetType.BULLSEYE);
      return BullseyeDataset.generateDefaultBullseyeDataset();
    }
  }

  public static Dataset fromJson(Json json) {
    Dataset dataset = new Dataset();
    json.asJsonArray().forEach(labeledDatapointJson -> dataset.add(LabeledDatapoint.fromJson(labeledDatapointJson)));
    return dataset;
  }

  public void add(LabeledDatapoint datapoint) {
    labeledDatapoints.add(datapoint);
  }

  public LabeledDatapoint get(int i) {
    return labeledDatapoints.get(i);
  }

  public int size() {
    return labeledDatapoints.size();
  }
  
  public Json toJson() {
    return Json.array(labeledDatapoints, LabeledDatapoint::toJson);
  }

  public static enum DatasetType {
    XOR(2, 1), BULLSEYE(2, 1);

    public final int inSize, outSize;

    private DatasetType(int inSize, int outSize) {
      this.inSize = inSize;
      this.outSize = outSize;
    }
  }
}
