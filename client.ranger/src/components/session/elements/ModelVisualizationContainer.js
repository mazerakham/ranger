import React, { Component } from 'react';

import PlainNeuralNetworkContainer from 'components/neuralnetwork/plainneuralnetwork/PlainNeuralNetworkContainer';
import RangerNetworkContainer from 'components/neuralnetwork/rangernetwork/RangerNetworkContainer';

export default class ModelVisualizationContainer extends Component {

  renderModelVisualization = () => {
    switch (this.props.modelType) {
      case 'plain': return (
        <React.Fragment>
          <div>Here is your plain neural network.</div>
          <div className="plain-nn svg div">
            <PlainNeuralNetworkContainer neuralNetwork={this.props.neuralNetwork} />
          </div>
        </React.Fragment>
      );
      case 'ranger': return (
        <React.Fragment>
        <div>Here is your Ranger network.</div>
        <div className="ranger-nn svg div">
          <RangerNetworkContainer neuralNetwork={this.props.neuralNetwork} displayNeuronInfo={this.props.displayNeuronInfo} />
        </div>
        </React.Fragment>
      );
      default: throw new Error('modelType ' + this.props.modelType + ' is not supported.');
    }
  }

  render() {
    return (
      <div className="Panel flexcolumn">
        {this.renderModelVisualization()}
      </div>
    );
  }
}