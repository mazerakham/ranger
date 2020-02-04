import React, { Component } from 'react';

import Neuron from './Neuron';
import SVG from 'components/meta/SVG';

export default class HiddenLayer extends Component {
  constructor(props) {
    super(props);
    this.coords = new HiddenLayerCoords();
  }
  render() {
    return (
      <SVG parentCoords={this.props.coords} coords={this.coords}>
        <Neuron coords={this.coords.getNeuronCoords(0)} />
        <Neuron coords={this.coords.getNeuronCoords(1)} />
        <Neuron coords={this.coords.getNeuronCoords(2)} />
        <Neuron coords={this.coords.getNeuronCoords(3)} />
        <Neuron coords={this.coords.getNeuronCoords(4)} />
      </SVG>
    );
  }
}

export class HiddenLayerCoords {
  constructor() {
    this.w = 1;
    this.h = 5;
  }

  getNeuronCoords = n => {
    return {
      x: 0,
      y: n,
      w: 1,
      h: 1
    }
  }
}
