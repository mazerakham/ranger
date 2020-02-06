import React, { Component } from 'react';

import PlainNeuralNetworkContainer from 'components/neuralnetwork/plainneuralnetwork/PlainNeuralNetworkContainer';

export default class NeuralNetworkPanel extends Component {

  render() {
    return (
      <div className="Session Panel">
        <div>Plain Neural Network.</div>
        <div>
          <PlainNeuralNetworkContainer neuralNetwork={this.props.neuralNetwork} />
        </div>
      </div>
    )
  }
}