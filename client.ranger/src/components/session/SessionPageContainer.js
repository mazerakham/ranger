import React, { Component } from 'react';

import SessionPage from './SessionPage';

import RangerClient from 'utils/RangerClient';

export default class SessionPageContainer extends Component {

  constructor(props) {
    super(props);
    this.rangerClient = new RangerClient();
    this.state = {
      stage: "loading"
    }
  }

  componentDidMount() {
    console.log(this.props.sessionOptions);
    this.rangerClient.newNeuralNetwork(
        this.props.sessionOptions.modelType,
        this.props.sessionOptions.neuralNetworkSpecs
    ).then(this.onNeuralNetworkCreated);

    this.rangerClient.desiredPlot(this.props.sessionOptions.datasetType).then(this.onDesiredPlotCreated);
  }

  onNeuralNetworkCreated = json => {
    console.log("Created new neural network: ");
    console.log(json.neuralNetwork);
    this.setState({
      neuralNetwork: json.neuralNetwork
    })
    this.rangerClient.neuralFunctionPlot(
        this.props.sessionOptions.datasetType, 
        this.props.sessionOptions.modelType, 
        json.neuralNetwork
    ).then(this.onNeuralFunctionPlotCreated);
  }

  onNeuralFunctionPlotCreated = json => {
    console.log("Created neural function plot:");
    console.log(json.plot);
    this.setState({
      plot: json.plot,
      stage: "finished"
    })
  }

  onDesiredPlotCreated = json => {
    console.log("Created desired plot:");
    console.log(json.plot);
    this.setState({
      desiredPlot: json.plot
    })
  }

  performTrainingStep = (batchSize, numSteps, learningRate) => {
    console.log("Sending neural network: ");
    console.log(this.state.neuralNetwork);
    this.rangerClient.train(this.props.sessionOptions.datasetType, this.state.neuralNetwork, batchSize, numSteps, learningRate).then(json => {
      console.log("Got back neural network: ");
      console.log(json.neuralNetwork);
      console.log("Got back plot: ");
      console.log(json.plot);
      this.setState({
        neuralNetwork: json.neuralNetwork,
        plot: json.plot
      });
      this.forceUpdate();
    })
  }

  backToHome = () => {
    this.props.app.loadPage('home');
  }

  render() {
    console.log(this.props.sessionOptions);
    switch (this.state.stage) {
      case "loading":
        return ( <div><h1>Creating Session.</h1></div>);
      case "finished":
        return ( 
          <SessionPage 
              sessionOptions={this.props.sessionOptions}
              neuralNetwork={this.state.neuralNetwork}
              desiredPlot={this.state.desiredPlot}
              plot={this.state.plot}
              performTrainingStep={this.performTrainingStep}
              backToHome={this.backToHome}
          /> 
        )
      default:
        throw new Error("Did not recognize stage " + this.state.stage);
    }
  }

}