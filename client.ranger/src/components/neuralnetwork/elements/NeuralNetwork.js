import React, { Component } from 'react';

import InputLayer, { InputLayerCoords } from './InputLayer';
import HiddenLayer, { HiddenLayerCoords } from './HiddenLayer';
import OutputLayer, { OutputLayerCoords } from './OutputLayer';
import W1Layer from './W1Layer';
import W2Layer from './W2Layer';

import SVG from 'components/meta/SVG';

import Coordinates from 'utils/Coordinates';

export default class NeuralNetwork extends Component {

  constructor(props) {
    super(props);
    this.coords = new NeuralNetworkCoords();
  }

  render() {
    return (
      <SVG className="NeuralNetwork" parentCoords={SVG.rootCoords()} coords={this.coords}>
        <rect x="0" y="0" width={this.coords.w} height={this.coords.h} style={{fill:"none", strokeWidth:0.1, stroke:"rgb(256,0,0)"}} />
        <InputLayer coords={this.coords.inputLayerEmbedding} neuralNetwork={this.props.neuralNetwork} />
        <HiddenLayer coords={this.coords.hiddenLayerEmbedding} neuralNetwork={this.props.neuralNetwork} />
        <OutputLayer coords={this.coords.outputLayerEmbedding} neuralNetwork={this.props.neuralNetwork} />
        <W1Layer connectionCoordinates={this.coords.w1ConnectionCoordinates} neuralNetwork={this.props.neuralNetwork} />
        <W2Layer connectionCoordinates={this.coords.w2ConnectionCoordinates} neuralNetwork={this.props.neuralNetwork} />
      </SVG>
    )
  }
}

export class NeuralNetworkCoords extends Coordinates {
  constructor() {
    super();
    this.w = 10;
    this.h = 10;
    this.inputLayerCoords = new InputLayerCoords();
    this.hiddenLayerCoords = new HiddenLayerCoords();
    this.outputLayerCoords = new OutputLayerCoords();
    this.inputLayerEmbedding = {
      x: 1,
      y: 4,
      w: 1,
      h: 2
    };
    this.hiddenLayerEmbedding = {
      x: 4,
      y: 2.5,
      w: 1,
      h: 5,
    };
    this.outputLayerEmbedding = {
      x: 7,
      y: 4.5,
      w: 1,
      h: 1
    };
    this.w1LayerEmbedding = {
      x: 0,
      y: 0,
      w: 10,
      h: 10
    }
    this.w2LayerEmbedding = {
      x: 0,
      y: 0,
      w: 10,
      h: 10
    }
  }

  w1ConnectionCoordinates = (i,j) => {
    const [x1i, y1i] = this.inputLayerCoords.getNeuronCenter(i);
    const [x1, y1] = this.fromChildCoords([x1i, y1i], this.inputLayerEmbedding, this.inputLayerCoords);
    const [x2h, y2h] = this.hiddenLayerCoords.getNeuronCenter(j);
    const [x2, y2] = this.fromChildCoords([x2h, y2h], this.hiddenLayerEmbedding, this.hiddenLayerCoords);
    return {x1:x1, y1:y1, x2:x2, y2:y2};
  }

  w2ConnectionCoordinates = (i,j) => {
    const [x1h, y1h] = this.hiddenLayerCoords.getNeuronCenter(i);
    const [x1, y1] = this.fromChildCoords([x1h, y1h], this.hiddenLayerEmbedding, this.hiddenLayerCoords);
    const [x2o, y2o] = this.outputLayerCoords.getNeuronCenter(j);
    const [x2, y2] = this.fromChildCoords([x2o, y2o], this.outputLayerEmbedding, this.outputLayerCoords);
    return {x1:x1, y1:y1, x2:x2, y2:y2};
  }
}
