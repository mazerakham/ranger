package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;
import static ox.util.Functions.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ox.Json;
import ranger.math.Vector;
import ranger.nn.ranger.Neuron.NeuronType;

public class Layer {

  // With this probability, a new dendrite will be grown to a predecessor by a newly-created neuron.
  private static final double DENDRITE_SPARSITY_CONSTANT = 0.3;

  /**
   * The neurons.
   */
  private List<Neuron> neurons = new ArrayList<>();

  /**
   * Forward prop stimuli & activations.
   */
  private SignalVector dendriteStimulus;
  private Vector preAxonActivation;
  private SignalVector axonActivation;

  /**
   * Backprop signals.
   */
  private Vector axonSignal;
  private Vector preAxonSignal;
  private Vector dendriteSignal;

  public void growNewNeuron(Layer prev, Layer next, Random random) {
    Neuron neuron = Neuron.hiddenNeuron();
    neurons.add(neuron);
    neuron.initializeDendrites(DendriteMask.randomMask(prev, DENDRITE_SPARSITY_CONSTANT, random), random);
    next.maybeGrowDendritesTo(this.size() - 1, random);
  }

  public void maybeGrowDendritesTo(int neuronIndex, Random random) {
    // Each neuron has a const / (# dendrites) probability of adding this neuron to their mask.
    for (Neuron neuron : neurons) {
      neuron.maybeGrowDendritesTo(neuronIndex, random);
    }
  }

  public Layer withNeuron(Neuron neuron) {
    neurons.add(neuron);
    return this;
  }

  public Layer initializeDendritesSparse(Layer predecessor, Random random) {
    for (Neuron neuron : neurons) {
      checkState(neuron.type == NeuronType.HIDDEN);
      neuron.initializeDendrites(DendriteMask.randomMask(predecessor, DENDRITE_SPARSITY_CONSTANT, random), random);
    }
    return this;
  }

  public Layer initializeDendritesFull(Layer predecessor, Random random) {
    for (Neuron neuron : neurons) {
      checkState(neuron.type == NeuronType.OUTPUT);
      neuron.initializeDendrites(DendriteMask.fullMask(predecessor), random);
    }
    return this;
  }

  public Layer loadAxonActivation(SignalVector activationSignalVector) {
    this.axonActivation = activationSignalVector;
    return this;
  }


  public Layer loadDendriteStimulus(SignalVector activation) {
    this.dendriteStimulus = activation;
    for (int i = 0; i < neurons.size(); i++) {
      neurons.get(i).loadDendriteStimulus(dendriteStimulus); // The neuron will use its mask and signal strength.
    }
    return this;
  }

  public void computeAxonActivation() {
    checkState(dendriteStimulus != null, "Cannot compute activation without stimulus.");
    SignalVector axonActivation = new SignalVector();
    for (Neuron neuron : neurons) {
      axonActivation.signals.add(neuron.computeAxonActivation());
    }
    this.axonActivation = axonActivation;
  }

  public SignalVector getAxonActivation() {
    return this.axonActivation;
  }

  public void clearActivations() {
    this.dendriteStimulus = null;
    this.preAxonActivation = null;
    this.axonActivation = null;
  }

  public Layer loadAxonSignal(Vector signal) {
    this.axonSignal = signal;
    checkState(signal.size() == this.neurons.size());
    for (int i = 0; i < signal.size(); i++) {
      neurons.get(i).loadAxonSignal(signal.getEntry(i));
    }
    return this;
  }

  public Layer computeDendriteSignal(Layer prev) {
    Vector totalDendriteSignal = Vector.zeros(prev.size());
    for (Neuron neuron : neurons) {
      totalDendriteSignal.plus(neuron.dendriteMask.unMask(neuron.computeDendriteSignal(), prev.size()));
    }
    this.dendriteSignal = totalDendriteSignal;
    return this;
  }

  public Vector getDendriteSignal() {
    return this.dendriteSignal;
  }

  /**
   * Construct a new layer consisting of identity neurons back to the given layer.
   */
  public static Layer identityLayer(Layer layer) {
    Layer ret = new Layer();
    for (int i = 0; i < layer.size(); i++) {
      ret.withNeuron(Neuron.identityNeuron(i, layer.neurons.get(i).s));
    }
    return ret;
  }

  public static Layer inputLayer(int inSize) {
    Layer ret = new Layer();
    for (int i = 0; i < inSize; i++) {
      ret.withNeuron(Neuron.inputNeuron());
    }
    return ret;
  }

  public static Layer outputLayer(int inSize, int outSize, Layer inputLayer) {
    Layer ret = new Layer();
    for (int i = 0; i < outSize; i++) {
      ret.withNeuron(Neuron.outputNeuron(inSize));
    }
    return ret;
  }

  /**
   * Update state, dendrites, bias for each neuron. Kill them off if they meet the criteria for death.
   * 
   * 
   */
  public void updateNeurons(Layer prev, Layer next) {
    for (int i = 0; i < size(); i++) {
      Neuron curr;
      do {
        curr = neurons.get(i);
        curr.update();
        if (curr.isDead()) {
          neurons.remove(i);
          next.remapDendrites(i);
        }
      } while (curr.isDead() && i < size());
    }
  }

  private void remapDendrites(int i) {
    for (Neuron neuron : neurons) {
      neuron.remapDendrites(i);
    }
  }

  public boolean isOnlyIdentity() {
    for (int i = 0; i < neurons.size(); i++) {
      if (neurons.get(i).type != NeuronType.IDENTITY) {
        return false;
      }
    }
    return true;
  }

  public int size() {
    return neurons.size();
  }

  public static Layer fromJson(Json json) {
    Layer ret = new Layer();
    ret.neurons = map(json.getJson("neurons").asJsonArray(), Neuron::fromJson);
    return ret;
  }

  public Json toJson() {
    return Json.object()
        .with("neurons", Json.array(neurons, Neuron::toJson));
  }

}
