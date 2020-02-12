import React, { Component } from 'react';

import PlainControlSection from './PlainControlSection';

import RangerClient from 'utils/RangerClient';

export default class PlainControlSectionContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      numSteps: 1,
      batchSize: 50,
      learningRate: 0.01
    }
    this.rangerClient = new RangerClient()
  }

  onNumStepsChange = (numSteps) => {
    this.setState({numSteps: numSteps });
  }

  onBatchSizeChange = (batchSize) => {
    this.setState({batchSize: batchSize });
  }

  onLearningRateChange = (learningRate) => {
    this.setState({learningRate: learningRate });
  }

  performTrainingStep = () => {
    console.log("Sending neural network: ");
    console.log(this.props.neuralNetwork);
    this.rangerClient.train(
        this.props.datasetType, 
        this.props.neuralNetwork, 
        parseInt(this.state.batchSize),
        parseInt(this.state.numSteps), 
        parseFloat(this.state.learningRate)
    ).then(json => {
      console.log("Got back neural network: ");
      console.log(json.neuralNetwork);
      console.log("Got back plot: ");
      console.log(json.plot);
      this.props.updateNeuralNetwork(json.neuralNetwork);
      this.props.updatePlot(json.plot);
    })
  }

  render() {
    return (
      <PlainControlSection 
          numSteps={this.state.numSteps}
          onNumStepsChange={this.onNumStepsChange}
          batchSize={this.state.batchSize}
          onBatchSizeChange={this.onBatchSizeChange}
          learningRate={this.state.learningRate}
          onLearningRateChange={this.onLearningRateChange}
          performTrainingStep={this.performTrainingStep}
      />
    )
  }
}