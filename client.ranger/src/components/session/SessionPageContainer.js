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
        this.props.sessionOptions.datasetType, 
        this.props.sessionOptions.numHiddenLayers, 
        this.props.sessionOptions.layerSizes
    ).then(this.onNeuralNetworkCreated);

    this.rangerClient.desiredPlot(this.props.sessionOptions.datasetType).then(this.onDesiredPlotCreated);
  }

  onNeuralNetworkCreated = json => {
    console.log("Created new neural network: ");
    console.log(json.neuralNetwork);
    this.setState({
      neuralNetwork: json.neuralNetwork
    })
    this.rangerClient.neuralFunctionPlot(this.props.sessionOptions.datasetType, json.neuralNetwork)
        .then(this.onNeuralFunctionPlotCreated);
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

  performTrainingStep = batchSize => {
    console.log("Doing training step with batch size: " + batchSize);
    this.rangerClient.train(this.props.sessionOptions.datasetType, this.state.neuralNetwork, batchSize).then(json => {
      this.setState({
        neuralNetwork: json.neuralNetwork
      });
    })
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
          /> 
        )
      default:
        throw new Error("Did not recognize stage " + this.state.stage);
    }
  }

}