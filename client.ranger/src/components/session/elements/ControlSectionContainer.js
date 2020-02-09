import React, { Component } from 'react';

import ControlSection from './controlsection/ControlSection';

export default class ControlSectionContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      numSteps: 1,
      batchSize: 50
    }
  }

  onNumStepsChange = (numSteps) => {
    this.setState({numSteps: numSteps ? parseInt(numSteps) : "" });
  }

  onBatchSizeChange = (batchSize) => {
    this.setState({batchSize: batchSize ? parseInt(batchSize) : "" });
  }

  render() {
    return (
      <ControlSection 
          batchSize={this.state.batchSize} 
          onBatchSizeChange={this.onBatchSizeChange} 
          numSteps={this.state.numSteps}
          onNumStepsChange={this.onNumStepsChange}
          performTrainingStep={this.props.performTrainingStep}
      />
    )
  }
}