package ranger.nn.ranger.neuron;

public class Dendrite {

  private Neuron left;
  private Neuron right;
  private double weight;

  public Dendrite(Neuron left, Neuron right, double weight) {
    this.left = left;
    this.right = right;
    this.weight = weight;
  }
}
