import React, { Component } from 'react';

import PlainNeuralNetworkContainer from 'components/neuralnetwork/plainneuralnetwork/PlainNeuralNetworkContainer';

export default class NeuralNetworkPanel extends Component {

  render() {
    return (
      <div className="Session Panel flexrow">
        <div>Here is your plain neural network.</div>
        <div className="plain-nn svg div">
          <PlainNeuralNetworkContainer neuralNetwork={this.props.neuralNetwork} />
        </div>
      </div>
    )
  }
}