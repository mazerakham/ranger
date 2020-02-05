import React, { Component } from 'react';

import NeuralNetworkPanel from 'components/neuralnetwork/NeuralNetworkPanel';
import NeuralNetworkFunctionPlotPanel from 'components/neuralnetwork/NeuralNetworkFunctionPlotPanel';

import 'styles/TrainingHistory.css'

export default class TrainingHistoryPage extends Component {

  render() {
    return (
      <div>
        <div>
          <h2>Training History Page</h2>
        </div>
        <div className="TrainingHistory Page">
          <NeuralNetworkPanel neuralNetwork={this.props.neuralNetwork} />
          <NeuralNetworkFunctionPlotPanel neuralNetwork={this.props.neuralNetwork} />
        </div>
      </div>
    );
  }

}
