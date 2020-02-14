import React, { Component } from 'react';

import PlainLayer, { PlainLayerCoords } from './elements/PlainLayer';
import NeuronConnection from './elements/NeuronConnection';

import Coordinates from 'utils/Coordinates';
import SVG from 'components/meta/SVG';
import { bRange, cartesian, enumerate } from 'utils/Numbers';

export default class PlainNeuralNetwork extends Component {

  constructor(props) {
    super(props);
    this.numLayers = props.neuralNetwork.specs.numLayers;
    this.layerSizes = props.neuralNetwork.specs.layerSizes;
    this.coords = new PlainNeuralNetworkCoords(this.numLayers);
  }

  renderLayers = () => {
    return bRange(this.numLayers).map(i => {
      return (
        <PlainLayer key={i} layerSize={this.layerSizes[i]} coords={this.coords.getLayerEmbedding(i, this.layerSizes[i])} /> 
      );
    })
  }

  renderConnections =() => {
    return bRange(this.numLayers - 1).map(l => {
      return enumerate(cartesian(bRange(this.layerSizes[l]), bRange(this.layerSizes[l+1]))).map(([key, [i,j]]) => {
        return (
          <NeuronConnection key={key} coords={this.coords.getConnectionCoords(l, i, j, this.numLayers, this.layerSizes)} />
        )
      });
    })
  }

  render() {
    return (
      <SVG parentCoords={SVG.rootCoords()} coords={this.coords}>
        {this.renderConnections()}
        {this.renderLayers()}
      </SVG>
    )
  }
}

class PlainNeuralNetworkCoords extends Coordinates {
  constructor(numLayers) {
    super();
    this.x = 0;
    this.y = 0;
    this.w = Math.max(3+2*numLayers, 10);
    this.h = 10;
  }

  getLayerEmbedding = (i, layerSize) => {
    return {
      x: 1 + 2 * i,
      y: Math.max(4.5 - layerSize / 2, 0),
      w: 1,
      h: Math.min(layerSize, 10)
    }
  }

  getConnectionCoords = (l, i, j, numLayers, layerSizes) => {
    const plainLayer1Coords = new PlainLayerCoords(layerSizes[l]);
    const plainLayer2Coords = new PlainLayerCoords(layerSizes[l+1]);
    const [x1, y1] = this.fromChildCoords(
      plainLayer1Coords.getNeuronCenter(i),
      this.getLayerEmbedding(l, layerSizes[l]),
      plainLayer1Coords
    );
    const [x2, y2] = this.fromChildCoords(
      plainLayer2Coords.getNeuronCenter(j),
      this.getLayerEmbedding(l+1, layerSizes[l+1]),
      plainLayer2Coords
    );
    return {x1: x1, y1: y1, x2: x2, y2: y2};
  }
}