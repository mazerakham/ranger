import React, { Component } from 'react';

export default class RangerControlSection extends Component {

  render() {
    return (
      <div className="Session Panel flexcolumn">
        <div className="buttons">
          <button onClick={this.props.performTrainingStep}>Perform Training Step</button>
        </div>
      </div>
    )
  }
}