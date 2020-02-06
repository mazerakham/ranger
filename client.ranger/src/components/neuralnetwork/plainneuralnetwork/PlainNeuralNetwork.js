import React, { Component } from 'react';

import Coordinates from 'utils/Coordinates';

import SVG from 'components/meta/SVG';

export default class PlainNeuralNetwork extends Component {

  render() {
    return (
      <SVG parentCoords={SVG.rootCoords()} coords={new PlainNeuralNetworkCoords()}>
        {renderLayers()}
        {renderConnections()}
      </SVG>
    )
  }
}

class PlainNeuralNetworkCoords extends Coordinates {
  constructor() {
    super();
    this.x = 0;
    this.y = 0;
    this.w = 10;
    this.h = 10;
  }
}