import React, { Component } from 'react';

import NewNeuralNetworkModal from './NewNeuralNetworkModal';

export default class NewNeuralNetworkModalContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {hiddenLayerSize: 5};
  }

  render() {
    return (
      <NewNeuralNetworkModal
          container={this}
          showing={this.props.showing}
          onSubmit={this.props.onSubmit}
          hide={this.props.hide}
          hiddenLayerSize={this.state.hiddenLayerSize}
      />
    )
  }
}
