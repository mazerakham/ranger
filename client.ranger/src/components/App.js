import React, {Component} from 'react';

import TestPageContainer from 'components/test/TestPageContainer';
import HomePageContainer from 'components/home/HomePageContainer';
import DataExplorationPageContainer from 'components/dataexploration/DataExplorationPageContainer';
import NeuralNetworkPageContainer from 'components/neuralnetwork/NeuralNetworkPageContainer';

import 'styles/App.css';

import RangerClient from 'utils/RangerClient';

export default class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      currentPage: "neuralNetwork",
      neuralNetwork: new RangerClient().createNeuralNetwork(5)
    };
  }

  loadPage = (pageName) => {
    this.setState({currentPage: pageName});
  }

  loadNeuralNetworkPage = (neuralNetwork) => {
    this.setState({
      currentPage: "neuralNetwork",
      neuralNetwork: neuralNetwork
    });
  }

  renderCurrentPage = () => {
    switch (this.state.currentPage) {
      case "test":
        return (<TestPageContainer />);
      case "home":
        return (<HomePageContainer app={this} />);
      case "dataExploration":
        return (<DataExplorationPageContainer app={this}/>);
      case "neuralNetwork":
        return (<NeuralNetworkPageContainer app={this} neuralNetwork={this.state.neuralNetwork}/>);
      default:
        throw new Error("Unknown page: " + this.state.currentPage);
    }
  }

  render() {
    return (
      <div className="App">
        {this.renderCurrentPage()}
      </div>
    )
  }
}
