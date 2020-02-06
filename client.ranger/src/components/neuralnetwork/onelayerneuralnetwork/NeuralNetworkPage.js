import React, { Component } from 'react';

import NeuralNetworkPanel from './NeuralNetworkPanel';
import 'styles/NeuralNetworkPage.css';

export default class NeuralNetworkPage extends Component {

  render() {
    return (
      <div className="NeuralNetwork Page">
        <h2>Neural Network Explorer</h2>
        <NeuralNetworkPanel neuralNetwork={this.props.neuralNetwork} />
      </div>
    )
  }
}
