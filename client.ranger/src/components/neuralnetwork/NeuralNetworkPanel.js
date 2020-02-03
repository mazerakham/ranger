import React, { Component } from 'react';

import NeuralNetwork from './elements/NeuralNetwork';

export default class NeuralNetworkPanel extends Component {

  constructor(props) {
    super(props);
    console.log("NeuralNetworkPanel.")
    console.log(props.neuralNetwork);
  }

  render() {
    return (
      <div className="NeuralNetworkPanel">
        <NeuralNetwork neuralNetwork={this.props.neuralNetwork} />
      </div>
    )
  }
}
