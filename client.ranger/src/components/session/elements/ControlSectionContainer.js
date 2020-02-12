import React, { Component } from 'react';

import RangerControlSectionContainer from './controlsection/RangerControlSectionContainer';
import PlainControlSectionContainer from './controlsection/PlainControlSectionContainer';

export default class ControlSectionContainer extends Component {

  render() {
    switch (this.props.modelType) {
      case 'ranger':
        return (
          <RangerControlSectionContainer 
              neuralNetwork={this.props.neuralNetwork}
              updateNeuralNetwork={this.props.updateNeuralNetwork}
              updatePlot={this.props.updatePlot}
              datasetType={this.props.datasetType}
          />
        );
      case 'plain':
        return (
          <PlainControlSectionContainer 
              neuralNetwork={this.props.neuralNetwork}
              updateNeuralNetwork={this.props.updateNeuralNetwork}
              updatePlot={this.props.updatePlot}
              datasetType={this.props.datasetType}
          />
        )
      default:
        throw new Error("Unknown modelType: " + this.props.modelType);
    }
  }
}