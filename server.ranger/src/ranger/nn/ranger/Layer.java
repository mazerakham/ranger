package ranger.nn.ranger;

import ranger.math.Vector;
import ranger.nn.ranger.neuron.Neurons;

public class Layer {

  private Neurons neurons;

  public Layer(int size) {
    this.neurons = new Neurons(size);
  }

  public Layer loadActivation(Vector activation) {
    this.neurons.loadActivation(activation);
    return this;
  }

  public Vector getActivation() {
    return this.neurons.getActivation();
  }

  public void propagateForward() {
    this.neurons.propagateForward();
  }

  public static void link(Layer first, Layer second) {
    Neurons.xavierLink(first.neurons, second.neurons);
  }
}
