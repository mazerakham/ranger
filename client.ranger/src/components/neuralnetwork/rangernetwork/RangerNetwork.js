import React, { Component } from 'react';

import RangerLayer, { RangerLayerCoords } from './elements/RangerLayer';
import NeuronConnection from './elements/NeuronConnection';

import { bRange, cartesian, enumerate } from 'utils/Numbers';
import SVG from 'components/meta/SVG';
import Coordinates from 'utils/Coordinates';

export default class RangerNetwork extends Component {

  constructor(props){
    super(props);
    this.getNeuralNetworkInfo();
  }

  componentDidUpdate(prevProps, prevState) {
    this.getNeuralNetworkInfo();
  }

  getNeuralNetworkInfo = () => {
    this.layers = this.props.rangerNetwork.layers;
    this.numLayers = this.layers.length;
    this.coords = new RangerNetworkCoords(this.numLayers);
    this.layerSizes = this.layers.map(layer => Object.keys(layer.neurons).length);

    // Enumerate the neurons in each layer.
    for (let layer of this.layers) {
      try {
        for (let [i, [uuid, neuron]] of enumerate(Object.entries(layer.neurons))) {
          layer.neurons[uuid].position = i;
        }
      } catch (e) {
        console.log("Layer");
        console.log(layer);
        throw e;
      }
    }
  }

  renderLayers = () => {
    return enumerate(this.layers).map(([i,layer]) => {
      return <RangerLayer 
          key={Math.random()} 
          layerSize={Object.keys(layer.neurons).length} 
          coords={this.coords.getLayerEmbedding(i, this.numLayers, Object.keys(layer.neurons).length)}
          neurons={layer.neurons}
      />
    });
  }

  renderConnections = () => {
    let ret = [];
    for (let l = 0; l < this.numLayers - 1; l++) {
      let layer1 = this.layers[l];
      let layer1Size = this.layerSizes[l];
      let layer2 = this.layers[l+1];
      let layer2Size = this.layerSizes[l+1];
      for (let [j, [uuid2, neuron2]] of enumerate(Object.entries(layer2.neurons))) {
        for (let [i, [uuid1, weight]] of enumerate(Object.entries(neuron2.dendrites))) {
          ret.push(
            <NeuronConnection 
                key={Math.random()} 
                coords={this.coords.getConnectionCoords(
                    l,
                    layer1.neurons[uuid1].position,
                    neuron2.position,
                    this.numLayers,
                    this.layerSizes
                )} 
            />
          );
        }
      }
      
    }
    return ret;
  }

  render() {
    return (
      <SVG parentCoords={SVG.rootCoords()} coords={new RangerNetworkCoords(this.numLayers)}>
        {this.renderConnections()}
        {this.renderLayers()}
      </SVG>
    )
  }
}

export class RangerNetworkCoords extends Coordinates {

  constructor(numLayers) {
    super();
    this.x = 0;
    this.y = 0;
    this.w = 1 + 2 * numLayers;
    this.h = 10;
  }

  getLayerEmbedding = (i, numLayers, layerSize) => {
    return {
      x: 1 + 2 * i,
      y: 4.5 - layerSize / 2,
      w: 1,
      h: layerSize
    }
  }

  getConnectionCoords = (l, i, j, numLayers, layerSizes) => {
    const rangerLayer1Coords = new RangerLayerCoords(layerSizes[l]);
    const rangerLayer2Coords = new RangerLayerCoords(layerSizes[l+1]);
    const [x1, y1] = this.fromChildCoords(
      rangerLayer1Coords.getNeuronCenter(i),
      this.getLayerEmbedding(l, numLayers, layerSizes[l]),
      rangerLayer1Coords
    );
    const [x2, y2] = this.fromChildCoords(
      rangerLayer2Coords.getNeuronCenter(j),
      this.getLayerEmbedding(l+1, numLayers, layerSizes[l+1]),
      rangerLayer2Coords
    );
    return {x1: x1, y1: y1, x2: x2, y2: y2};
  }
}