import React, { Component } from 'react';

import DataExplorationPage from './DataExplorationPage';
import NewNeuralNetworkModal from './NewNeuralNetworkModal';

export default class DataExplorationPageContainer extends Component {

  constructor(props) {
    super(props);
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

  submitCreateNeuralNetwork = () => {
    this.setState({showingModal: 'none'});
    console.log("Submitting command to create new neural network.");
  }

  hideModal = () => {
    this.setState({showingModal: 'none'});
    console.log("Modal closed.");
  }

  render() {
    return (
      <React.Fragment>
        <NewNeuralNetworkModal
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
