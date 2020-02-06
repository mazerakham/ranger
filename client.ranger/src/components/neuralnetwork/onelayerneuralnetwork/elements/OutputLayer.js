import React, { Component } from 'react';

import Neuron from './Neuron';
import SVG from 'components/meta/SVG';

export default class OutputLayer extends Component {
  constructor(props) {
    super(props);
    this.coords = new OutputLayerCoords();
  }
  render() {
    return (
      <SVG parentCoords={this.props.coords} coords={this.coords}>
        <Neuron coords={this.coords.getNeuronCoords(0)} />
      </SVG>
    );
  }
}

export class OutputLayerCoords {
  constructor() {
    this.w = 1;
    this.h = 1;
  }

  getNeuronCoords = n => {
    return {
      x: 0,
      y: n,
      w: 1,
      h: 1
    }
  }

  getNeuronCenter = i => {
    return [0.5, i + 0.5];
  }
}
