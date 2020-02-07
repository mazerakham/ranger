import React, { Component } from 'react';

export default class ControlSection extends Component {

  render() {
    return (
      <div className="Session Panel flexcolumn">
        <div>
          <span>Batch Size: </span>
          <input value={this.props.batchSize} onChange={(e) => this.props.onBatchSizeChange(e.target.value)}></input>
          <div className="buttons">
            <button onClick={() => this.props.performTrainingStep(this.props.batchSize)}>Perform Training Step</button>
          </div>
        </div>
      </div>
    )
  }
}