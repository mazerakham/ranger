import React, { Component } from 'react';

import RangerClient from 'utils/RangerClient';

export default class NeuralNetworkFunctionPlotPanel extends Component {

  constructor(props) {
    super(props);
    this.rangerClient = new RangerClient();
  }

  componentDidMount() {
    this.rangerClient.getNeuralFunctionPlot(this.props.neuralNetwork).then(plotData => {
      console.log("NeuralFunctionPlotPanel got this plot data back:");
      console.log(plotData.plot);

    });
  }

  componentDidUpdate(prevProps) {
    Plotly.newPlot(
        'myDiv',
        [{
          z: this.props.plot,
          type: 'contour',
          colorscale: 'Jet',
          dx: 0.4,
          x0: -1,
          dy: 0.4,
          y0: -1
        }],
        {title: 'Basic Contour Plot'}
    );
  }

  render() {
    return (
      <div className="NeuralNetworkFunctionPlotPanel">
        <div id='myDiv'></div>
      </div>
    );
  }
}
