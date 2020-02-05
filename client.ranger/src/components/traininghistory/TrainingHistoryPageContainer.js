import React, { Component } from 'react';

import TrainingHistoryPage from './TrainingHistoryPage';

import RangerClient from 'utils/RangerClient';

export default class TrainingHistoryPageContainer extends Component {

  constructor(props) {
    super(props);
    this.rangerClient = new RangerClient();
    this.state = {step: 0};
  }

  componentDidMount() {
    this.rangerClient.getTrainingHistory().then(history => {
      this.setState({history: history});
      console.log("Request officially finished with history:");
      console.log(history);
      this.rangerClient.getNeuralFunctionPlot(this.state.history[this.state.step]).then(plotData => {
        this.setState({plot: plotData.plot});
      });
    });
  }

  getStep = (step) => {
    this.setState({step: step});
    if (step) {
      console.log("New neural network for step: " + step);
      console.log(this.state.history[step]);
    }
    this.rangerClient.getNeuralFunctionPlot(this.state.history[this.state.step]).then(plotData => {
      console.log("TrainingHistoryPageContainer got plot:");
      console.log(plotData.plot);
      this.setState({plot: plotData.plot});
    });
  }

  render() {
    if (this.state.history) {
      return (
        <TrainingHistoryPage
            step = {this.state.step}
            getStep = {this.getStep}
            neuralNetwork = {this.state.history[this.state.step]}
            plot = {this.state.plot}
        />
      );
    } else {
      return <div><h1>Loading history.</h1></div>;
    }
  }
}
