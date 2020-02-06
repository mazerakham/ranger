import React, { Component } from 'react';

import ControlSection from './controlsection/ControlSection';

export default class ControlSectionContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      batchSize: 50
    }
  }

  onBatchSizeChange = (batchSize) => {
    this.setState({batchSize: batchSize});
  }

  render() {
    return (
      <ControlSection batchSize={this.state.batchSize} onBatchSizeChange={this.onBatchSizeChange} />
    )
  }
}