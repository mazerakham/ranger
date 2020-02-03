import React, { Component } from 'react';

import NeuralNetworkPage from './NeuralNetworkPage';

export default class NeuralNetworkPageContainer extends Component {

  render() {
    return (
      <NeuralNetworkPage
          container={this}
          neuralNetwork={this.props.neuralNetwork}
      />
    )
  }
}
