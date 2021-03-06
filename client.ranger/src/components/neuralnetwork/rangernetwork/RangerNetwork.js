import React, { Component } from 'react';

import RangerLayer, { RangerLayerCoords } from './elements/RangerLayer';
import NeuronConnection from './elements/NeuronConnection';

import { bRange, cartesian, enumerate } from 'utils/Numbers';
import SVG from 'components/meta/SVG';
import Coordinates from 'utils/Coordinates';

export default class RangerNetwork extends Component {

  constructor(props){
    super(props);
    console.log("Constructing RangerNetwork with props:");
    console.log(props);
    this.coords = new RangerNetworkCoords(props.numLayers);
  }

  renderLayers = () => {
    return enumerate(this.props.layers).map(([l,layer]) => {
      return <RangerLayer 
          key={Math.random()}
          l = {l}
          layerSize={Object.keys(layer.neurons).length} 
          coords={this.coords.getLayerEmbedding(l, this.props.numLayers, Object.keys(layer.neurons).length)}
          neurons={layer.neurons}
          displayNeuronInfo={this.props.displayNeuronInfo}
      />
    });
  }

  renderConnections = () => {
    let ret = [];
    for (let l = 0; l < this.props.numLayers - 1; l++) {
      let layer1 = this.props.layers[l];
      let layer1Size = Object.keys(layer1.neurons).length;
      let layer2 = this.props.layers[l+1];
      let layer2Size = Object.keys(layer2.neurons).length;
      for (let [j, [uuid2, neuron2]] of enumerate(Object.entries(layer2.neurons))) {
        for (let [i, [uuid1, weight]] of enumerate(Object.entries(neuron2.dendrites))) {
          ret.push(
            <NeuronConnection 
                key={Math.random()}
                weight={weight} 
                coords={this.coords.getConnectionCoords(
                    l,
                    layer1.neurons[uuid1].position,
                    neuron2.position,
                    this.props.numLayers,
                    this.props.layerSizes
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
      <SVG parentCoords={SVG.rootCoords()} coords={new RangerNetworkCoords(this.props.numLayers)}>
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
      y: 4.5 - Math.min(4.5, layerSize / 2),
      w: 1,
      h: Math.min(layerSize, 10)
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