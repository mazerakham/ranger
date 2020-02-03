import React, { Component } from 'react';

import DataExplorationPage from './DataExplorationPage';
import NewNeuralNetworkModalContainer from './NewNeuralNetworkModalContainer';

import RangerClient from 'utils/RangerClient';

export default class DataExplorationPageContainer extends Component {

  constructor(props) {
    super(props);
    this.rangerClient = new RangerClient();
    this.state = {showingModal: 'none'};
  }

  loadPage = (page) => this.props.app.loadPage(page);

  visualizeDataset = () => {
    alert('Data visualization is not yet supported.  Check again soon!');
  }

  loadNeuralNetwork = () => {
    alert('Loading a neural network is not yet supported.  Check again soon!');
  }

  createNeuralNetwork = () => {
    this.setState({showingModal: 'newNeuralNetwork'});
  }

  submitCreateNeuralNetwork = (hiddenLayerSize) => {
    this.setState({showingModal: 'none'});
    console.log("Submitting command to create new neural network with hidden layer size: " + hiddenLayerSize);
    const json = this.rangerClient.createNeuralNetwork(hiddenLayerSize);
    this.props.app.loadNeuralNetworkPage(json);
  }

  hideModal = () => {
    this.setState({showingModal: 'none'});
    console.log("Modal closed.");
  }

  render() {
    return (
      <React.Fragment>
        <NewNeuralNetworkModalContainer
            container={this}
            showing={this.state.showingModal === 'newNeuralNetwork'}
            onSubmit={this.submitCreateNeuralNetwork}
            hide={this.hideModal}
        />
        <DataExplorationPage container={this} />
      </React.Fragment>
    )
  }
}
