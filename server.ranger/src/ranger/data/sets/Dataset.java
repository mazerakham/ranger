package ranger.data.sets;

import java.util.ArrayList;
import java.util.List;

import ranger.data.LabeledDatapoint;

public class Dataset {

  public List<LabeledDatapoint> labeledDatapoints = new ArrayList<>();

  public void add(LabeledDatapoint datapoint) {
    labeledDatapoints.add(datapoint);
  }

  public LabeledDatapoint get(int i) {
    return labeledDatapoints.get(i);
  }

  public int size() {
    return labeledDatapoints.size();
  }
}
