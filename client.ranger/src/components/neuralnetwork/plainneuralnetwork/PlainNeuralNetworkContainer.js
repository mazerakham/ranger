import React, { Component } from 'react';

import PlainNeuralNetwork from './PlainNeuralNetwork';

export default class PlainNeuralNetworkContainer extends Component {

  render() {
    return (
      <PlainNeuralNetwork neuralNetwork={this.props.neuralNetwork} />
    )
  }
}