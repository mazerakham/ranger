package ranger.nn.ranger.neuron;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dendrites {

  private final List<Dendrite> dendrites = new ArrayList<>();

  public static void xavierConnection(Neuron leftNeuron, Neuron rightNeuron, int fanIn) {
    double weight = new Random().nextGaussian() * Math.sqrt(2.0 / fanIn);
    Dendrite dendrite = new Dendrite(leftNeuron, rightNeuron, weight);
    leftNeuron.addRightDendrite(dendrite);
    rightNeuron.addLeftDendrite(dendrite);
  }

  public void add(Dendrite dendrite) {
    this.dendrites.add(dendrite);
  }
}
