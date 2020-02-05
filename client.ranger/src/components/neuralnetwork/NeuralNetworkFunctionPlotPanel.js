import React, { Component } from 'react';

export default class NeuralNetworkFunctionPlotPanel extends Component {

  constructor(props) {
    super(props);
    console.log(this.plotly);
  }

  componentDidMount() {
    let data = [
      {
        z: [
          [10, 10.625, 12.5, 15.625, 20],
          [5.625, 6.25, 8.125, 11.25, 15.625],
          [2.5, 3.125, 5.0, 8.125, 12.5],
          [0.625, 1.25, 3.125, 6.25, 10.625],
          [0, 0.625, 2.5, 5.625, 10]
        ],
        type: 'contour'
      }
    ];

    let layout = {
      title: 'Basic Contour Plot'
    }

    Plotly.newPlot('myDiv', data, layout);
  }

  render() {
    return (
      <div className="NeuralNetworkFunctionPlotPanel">
        <div id='myDiv'></div>
      </div>
    )
  }
}
