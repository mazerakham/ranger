package ranger.nn.ranger.neuron;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import ranger.math.Vector;

public class Neurons {

  private List<Neuron> neurons;

  public Neurons(int size) {
    for (int i = 0; i < size; i++) {
      neurons.add(new Neuron());
    }
  }

  public static void xavierLink(Neurons left, Neurons right) {
    int fanIn = left.size();
    for (Neuron rightNeuron : right.neurons) {
      for (Neuron leftNeuron : left.neurons) {
        Dendrites.xavierConnection(leftNeuron, rightNeuron, fanIn);
      }
    }
  }

  public Vector getActivation() {
    return new Vector(neurons, neuron -> neuron.getActivation());
  }

  public void loadActivation(Vector activation) {
    checkState(neurons.size() == activation.size());
    for (int i = 0; i < activation.size(); i++) {
      neurons.get(i).loadActivation(activation.getEntry(i));
    }
  }

  public void propagateForward() {
    for (Neuron neuron : neurons) {
      neuron.propagateForward();
    }
  }

  public int size() {
    return neurons.size();
  }
}
