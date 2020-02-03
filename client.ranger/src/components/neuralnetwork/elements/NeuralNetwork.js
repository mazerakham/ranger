import React, { Component } from 'react';

import InputLayer from './InputLayer';
import HiddenLayer from './HiddenLayer';
import OutputLayer from './OutputLayer';
import W1Layer from './W1Layer';
import W2Layer from './W2Layer';

import SVG from 'components/meta/SVG';

export default class NeuralNetwork extends Component {

  constructor(props) {
    super(props);
    this.coords = new NeuralNetworkCoords();
  }

  render() {
    return (
      <SVG className="NeuralNetwork" parentCoords={SVG.rootCoords()} coords={this.coords} root>
        <rect x="0" y="0" width={this.coords.w} height={this.coords.h} style={{fill:"none", strokeWidth:0.1, stroke:"rgb(256,0,0)"}} />
        <InputLayer coords={this.coords.inputLayerCoords} neuralNetwork={this.props.neuralNetwork} />
        <HiddenLayer coords={this.coords.hiddenLayerCoords} neuralNetwork={this.props.neuralNetwork} />
        <OutputLayer coords={this.coords.outputLayerCoords} neuralNetwork={this.props.neuralNetwork} />
        <W1Layer coords={this.coords.w1LayerCoords} neuralNetwork={this.props.neuralNetwork} />
        <W2Layer coords={this.coords.w2LayerCoords} neuralNetwork={this.props.neuralNetwork} />
      </SVG>
    )
  }
}

class NeuralNetworkCoords {
  constructor() {
    this.w = 10;
    this.h = 10;
    this.inputLayerCoords = {
      x: 1,
      y: 4,
      w: 1,
      h: 2
    };
    this.hiddenLayerCoords = {
      x: 4,
      y: 1,
      w: 1,
      h: 8,
    };
    this.outputLayerCoords = {
      x: 7,
      y: 4.5,
      w: 1,
      h: 1
    };
    this.w1LayerCoords = {
      x: 0,
      y: 0,
      w: 10,
      h: 10
    }
    this.w2LayerCoords = {
      x: 0,
      y: 0,
      w: 10,
      h: 10
    }
  }
}
