import React, { Component } from 'react';

import SVG from 'components/meta/SVG';

import { InputLayerCoords } from './InputLayer';
import { HiddenLayerCoords } from './HiddenLayer';
import { NeuralNetworkCoords } from './NeuralNetwork';
import NeuronConnection from './NeuronConnection';

export default class W1Layer extends Component {
  constructor(props) {
    super(props);
    this.coords = new W1LayerCoords();
    this.inputLayerCoords = new InputLayerCoords();
    this.hiddenLayerCoords = new HiddenLayerCoords();
    this.neuralNetworkCoords = new NeuralNetworkCoords();
    this.myCoordsInParent = this.neuralNetworkCoords.w1LayerCoords;
  }

  renderConnections = () => {
    const f = (a, b) => [].concat(...a.map(d => b.map(e => [].concat(d, e))));
    const cartesian = (a, b, ...c) => (b ? cartesian(f(a, b), ...c) : a);
    return cartesian([0,1],[0,1,2,3,4]).map(pair => this.renderConnection(pair[0], pair[1]));
  }

  renderConnection = (i,j) => {
    return <NeuronConnection coords={computeConnectionCoords(i,j)} />
  }

  computeConnectionCoords = (i,j) => {
    const [x1, y1] = fromInputCoordsToW1Coords(...this.inputLayerCoords.getNeuronCenter(i));
    const [x2, y2] = fromHiddenCoordsToW1Coords(this.hiddenLayerCoords.getNeuronCenter(j));
    return {x1: x1, y1: y1, x2: x2, y2: y2};
  }

  fromInputCoordsToW1Coords = (inputX, inputY) => {
    return [

    ]
  }

  render() {
    return (
      <SVG parentCoords={this.props.coords} coords={this.coords}>
        {this.renderConnections()}
      </SVG>
    );
  }
}

class W1LayerCoords {
  constructor() {
    this.x = 0;
    this.y = 0;
    this.w = 10;
    this.h = 10;
  }
}
