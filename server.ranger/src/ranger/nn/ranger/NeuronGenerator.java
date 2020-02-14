package ranger.nn.ranger;

import java.util.List;
import java.util.Random;

public class NeuronGenerator {

  public static final double GROWTH_CONSTANT = 0.5;

  public void growNewNeurons(List<Layer> layers, double totalWeight, int layerIndex, Random random) {
    Layer prev = layers.get(layerIndex - 1);
    Layer curr = layers.get(layerIndex);
    Layer next = layers.get(layerIndex + 1);
    double growthProba = GROWTH_CONSTANT * curr.size() / totalWeight;
    if (random.nextDouble() < growthProba) {
      curr.growNewNeuron(prev, next, random);
    }
  }

}
