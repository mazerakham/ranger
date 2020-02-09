import React, {Component} from 'react';

import TestPageContainer from 'components/test/TestPageContainer';
import HomePageContainer from 'components/home/HomePageContainer';
import DataExplorationPageContainer from 'components/dataexploration/DataExplorationPageContainer';
import NewSessionContainer from 'components/newsession/NewSessionContainer';
import SessionPageContainer from 'components/session/SessionPageContainer';
import NeuralNetworkPageContainer from 'components/neuralnetwork/onelayerneuralnetwork/NeuralNetworkPageContainer';
import TrainingHistoryPageContainer from 'components/traininghistory/TrainingHistoryPageContainer';

import 'styles/App.css';

import RangerClient from 'utils/RangerClient';

export default class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      currentPage: "home",
      sessionOptions: {
        datasetType: 'xor',
        modelType: 'plain',
        numHiddenLayers: 2,
        layerSizes: [3,5]
      }
    };

    this.rangerClient = new RangerClient();
    this.rangerClient.newNeuralNetwork(
        this.state.sessionOptions.datasetType, 
        this.state.sessionOptions.numHiddenLayers, 
        this.state.sessionOptions.layerSizes
    );
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

  startNewSession = (sessionOptions) => {
    this.setState({
      sessionOptions: sessionOptions,
      currentPage: "session"
    })
  }

  renderCurrentPage = () => {
    switch (this.state.currentPage) {
      case "test":
        return (<TestPageContainer />);
      case "home":
        return (<HomePageContainer app={this} />);
      case "dataExploration":
        return (<DataExplorationPageContainer app={this}/>);
      case "newSession":
        return (<NewSessionContainer app={this}/>);
      case "session":
        return (<SessionPageContainer app={this} sessionOptions={this.state.sessionOptions} />);
      case "neuralNetwork":
        return (<NeuralNetworkPageContainer app={this} neuralNetwork={this.state.neuralNetwork}/>);
      case "trainingHistory":
        return (<TrainingHistoryPageContainer app={this} />);
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
