import React, { Component } from 'react';

import NewNeuralNetworkModal from './NewNeuralNetworkModal';

import 'styles/DataExplorationPage.css';

export default class DataExplorationPage extends Component {
  render() {
    return (
      <div className="DataExplorationPage">
        <h2>Data Exploration Page!</h2>
        <div className="buttons">
          <button onClick={() => this.props.container.loadPage("home")}>Back to Home</button>
          <button onClick={this.props.container.visualizeDataset}>Visualize Dataset</button>
          <button onClick={this.props.container.loadNeuralNetwork}>Load Neural Network</button>
          <button onClick={this.props.container.createNeuralNetwork}>Create Neural Network</button>
        </div>
      </div>
    )
  }
}
