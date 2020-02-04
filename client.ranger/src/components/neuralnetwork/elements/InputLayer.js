import React, { Component } from 'react';

import Neuron from './Neuron';
import SVG from 'components/meta/SVG';

export default class InputLayer extends Component {
  constructor(props) {
    super(props);
    this.coords = new InputLayerCoords();
  }
  render() {
    return (
      <SVG parentCoords={this.props.coords} coords={this.coords}>
        <Neuron coords={this.coords.getNeuronCoords(0)} />
        <Neuron coords={this.coords.getNeuronCoords(1)} />
      </SVG>
    );
  }
}

export class InputLayerCoords {
  constructor() {
    this.w = 1;
    this.h = 2;
  }

  getNeuronCoords = n => {
    return {
      x: 0,
      y: n,
      w: 1,
      h: 1
    }
  }

  getNeuronCenter = n => [0.5, n+0.5];
}
