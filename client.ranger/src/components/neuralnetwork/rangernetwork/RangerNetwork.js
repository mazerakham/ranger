import React, { Component } from 'react';

import RangerLayer, { RangerLayerCoords } from './elements/RangerLayer';
import NeuronConnection from './elements/NeuronConnection';

import { bRange, cartesian, enumerate } from 'utils/Numbers';
import SVG from 'components/meta/SVG';
import Coordinates from 'utils/Coordinates';

export default class RangerNetwork extends Component {

  constructor(props){
    super(props);
    console.log("Constructing RangerNetwork.  props:");
    console.log(props);
    console.log("props.rangerNetwork:");
    console.log(props.rangerNetwork);
    this.coords = new RangerNetworkCoords();
    this.layers = props.rangerNetwork.layers;
    this.numLayers = this.layers.length;
    this.layerSizes = this.layers.map(layer => layer.size);
  }

  renderLayers = () => {
    return bRange(this.layers.length).map(i => {
      return (
        <RangerLayer key={i} layerSize={this.layerSizes[i]} coords={this.coords.getLayerEmbedding(i, this.numLayers, this.layerSizes[i])} /> 
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

  getLayerEmbedding = (i, layerSize) => {
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