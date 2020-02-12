import React, { Component } from 'react';

import RangerControlSection from './RangerControlSection';

import RangerClient from 'utils/RangerClient';

export default class RangerControlSectionContainer extends Component { 

  constructor(props) {
    super(props);
    this.rangerClient = new RangerClient();
  }

  performTrainingStep = () => {
    console.log("Sending neural network: ");
    console.log(this.props.neuralNetwork);
    this.rangerClient.trainRanger(
        this.props.datasetType, 
        this.props.neuralNetwork
    ).then(json => {
      console.log("Got back neural network: ");
      console.log(json.neuralNetwork);
      console.log("Got back plot: ");
      console.log(json.plot);
      this.props.updateNeuralNetwork(json.neuralNetwork);
      this.props.updatePlot(json.plot);
    })
  }

  render () {
    return (
      <RangerControlSection performTrainingStep={this.performTrainingStep} />
    )
  }
}