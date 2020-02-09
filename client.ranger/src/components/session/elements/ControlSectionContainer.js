import React, { Component } from 'react';

import ControlSection from './controlsection/ControlSection';

export default class ControlSectionContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      numSteps: 1,
      batchSize: 50,
      learningRate: 0.01
    }
  }

  onNumStepsChange = (numSteps) => {
    this.setState({numSteps: numSteps ? parseInt(numSteps) : "" });
  }

  onBatchSizeChange = (batchSize) => {
    this.setState({batchSize: batchSize ? parseInt(batchSize) : "" });
  }

  onLearningRateChange = (learningRate) => {
    this.setState({learningRate: learningRate ? parseFloat(learningRate) : "" });
  }

  performTrainingStep = () => {
    this.props.performTrainingStep(this.state.numSteps, this.state.batchSize, this.state.learningRate);
  }

  render() {
    return (
      <ControlSection 
          batchSize={this.state.batchSize} 
          onBatchSizeChange={this.onBatchSizeChange} 
          numSteps={this.state.numSteps}
          onNumStepsChange={this.onNumStepsChange}
          learningRate={this.state.learningRate}
          onLearningRateChange={this.onLearningRateChange}
          performTrainingStep={this.performTrainingStep}
      />
    )
  }
}