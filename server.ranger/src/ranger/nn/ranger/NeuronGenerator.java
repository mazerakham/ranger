package ranger.nn.ranger;

import java.util.List;
import java.util.Random;

import ox.Log;

public class NeuronGenerator {

  public static final double GROWTH_CONSTANT = 1.2;

  public void growNewNeurons(List<Layer> layers, double totalWeight, int layerIndex, Random random) {
    Layer prev = layers.get(layerIndex - 1);
    Layer curr = layers.get(layerIndex);
    // For even indices, we've grown an identity layer in front of us. Thus it makes sense to request connections from
    // the layer after that.
    Layer next = layerIndex % 2 == 0 ? layers.get(layerIndex + 2) : layers.get(layerIndex + 1);
    double growthProba = GROWTH_CONSTANT * curr.getGrowthRate()
        / totalWeight;
    Log.debug("growthProba %.2f for layer %d", growthProba, layerIndex);
    if (random.nextDouble() < growthProba) {
      Neuron neuron = curr.growNewNeuron(prev, next, random);
      if (layerIndex % 2 == 0) {
        layers.get(layerIndex + 1).withNeuron(Neuron.identityNeuron(neuron));
      }
    }
  }

}
