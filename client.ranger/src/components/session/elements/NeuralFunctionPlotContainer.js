import React, { Component } from 'react';

import ContourPlot from 'components/meta/ContourPlot';

export default class NeuralFunctionPlotContainer extends Component {

  render() {

    return (
      <div className="NeuralNetworkFunctionPlot Panel flexcolumn">
        <span>Current</span>
        <ContourPlot width={280 * 0.8} height={180 * 0.8} z={this.props.plot} id="actualPlot" />
      </div>
    );
  }
  
}