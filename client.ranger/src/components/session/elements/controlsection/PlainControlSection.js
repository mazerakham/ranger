import React, { Component } from 'react';

export default class PlainControlSection extends Component {

  render() {
    return (
      <div className="Session Panel flexcolumn">
        <div>
          <span>Number of Training Steps:</span>
          <input value={this.props.numSteps} onChange={(e) => this.props.onNumStepsChange(e.target.value)} />
        </div>
        <div>
          <span>Batch Size: </span>
          <input value={this.props.batchSize} onChange={(e) => this.props.onBatchSizeChange(e.target.value)} />
        </div>
        <div>
          <span>Learning Rate: </span>
          <input value={this.props.learningRate} onChange={(e) => this.props.onLearningRateChange(e.target.value)}></input>
        </div>
        <div className="buttons">
          <button onClick={this.props.performTrainingStep}>Perform Training Step</button>
        </div>
      </div>
    )
  }
}