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
    this.rangerClient.newNeuralNetwork(this.props.sessionOptions.modelType, this.props.sessionOptions.neuralNetworkSpecs)
        .then(this.onNeuralNetworkCreated);
    this.rangerClient.desiredPlot(this.props.sessionOptions.datasetType)
        .then(this.onDesiredPlotCreated);
  }

  onNeuralNetworkCreated = json => {
    console.log("Created new neural network: ");
    console.log(json.neuralNetwork);
    this.updateNeuralNetwork(json.neuralNetwork);
    this.rangerClient.neuralFunctionPlot(
        this.props.sessionOptions.datasetType, 
        this.props.sessionOptions.modelType, 
        json.neuralNetwork
    ).then(this.onNeuralFunctionPlotCreated);
  }

  onDesiredPlotCreated = json => {
    console.log("Created desired plot:");
    console.log(json.plot);
    this.setState({
      desiredPlot: json.plot
    })
  }

  onNeuralFunctionPlotCreated = json => {
    console.log("Created neural function plot:");
    console.log(json.plot);
    this.setState({
      plot: json.plot,
      stage: "finished"
    })
  }

  updateNeuralNetwork = neuralNetwork => this.setState({neuralNetwork: neuralNetwork});

  updatePlot = plot => this.setState({plot: plot});

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
              modelType={this.props.sessionOptions.modelType}

              neuralNetwork={this.state.neuralNetwork}
              updateNeuralNetwork={this.updateNeuralNetwork}
              
              desiredPlot={this.state.desiredPlot}

              plot={this.state.plot}
              updatePlot={this.updatePlot}

              backToHome={this.backToHome}
          /> 
        )
      default:
        throw new Error("Did not recognize stage " + this.state.stage);
    }
  }

}