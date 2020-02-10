package ranger.nn.ranger.neuron;

public class Neuron {

  private double activation;

  private Dendrites leftDendrites = new Dendrites();

  private Dendrites rightDendrites = new Dendrites();

  public void addLeftDendrite(Dendrite dendrite) {
    leftDendrites.add(dendrite);
  }

  public void addRightDendrite(Dendrite dendrite) {
    rightDendrites.add(dendrite);
  }

  public void loadActivation(double d) {
    this.activation = d;
  }

  public double getActivation() {
    return this.activation;
  }
}
