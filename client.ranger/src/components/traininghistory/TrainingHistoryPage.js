import React, { Component } from 'react';

import NeuralNetworkPanel from 'components/neuralnetwork/NeuralNetworkPanel';
import NeuralNetworkFunctionPlotPanel from 'components/neuralnetwork/NeuralNetworkFunctionPlotPanel';

import 'styles/TrainingHistory.css'

export default class TrainingHistoryPage extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <div>
          <h2>Training History Page</h2>
          <input type="range" min="0" max="999" step="50" value={this.props.step} onChange={e => this.props.getStep(e.target.value)} />
        </div>
        <div className="TrainingHistory Page">
          <NeuralNetworkPanel neuralNetwork={this.props.neuralNetwork} />
          <NeuralNetworkFunctionPlotPanel plot={this.props.plot} />
        </div>
      </div>
    );
  }

}
