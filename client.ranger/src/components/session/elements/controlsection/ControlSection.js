import React, { Component } from 'react';

export default class ControlSection extends Component {

  render() {
    return (
      <div className="Session Panel flexcolumn">
          <div>
            <span>Number of Training Steps:</span>
            <input value={this.props.numSteps} onChange={(e) => {this.props.onNumStepsChange(e.target.value)}} />
          </div>
          <div>
            <span>Batch Size: </span>
            <input value={this.props.batchSize} onChange={(e) => this.props.onBatchSizeChange(e.target.value)} />
          </div>          
          <div className="buttons">
            <button onClick={() => this.props.performTrainingStep(this.props.batchSize, this.props.numSteps)}>
              Perform Training Step
            </button>
          </div>
      </div>
    )
  }
}