import React, { Component } from 'react';

export default class NeuralFunctionPlotContainer extends Component {

  componentDidMount() {
    Plotly.newPlot(
      this.props.id,
      [{
        z: this.props.z,
        type: 'contour',
        colorscale: 'Jet',
        dx: 0.4,
        x0: -1,
        dy: 0.4,
        y0: -1
      }],
      {
        title: this.props.title,
        width: this.props.width,
        height: this.props.height,
        margin: {
          l: 0,
          r: 0,
          b: 5,
          t: 5,
        }
      }
    );
  }

  render() {
    return (
      <div id={this.props.id}></div>
    );
  }
}