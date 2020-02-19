import React, { Component } from 'react';

import ChoosePlainNetworkDetailsPage from './ChoosePlainNetworkDetailsPage';

export default class ChoosePlainNetworkDetailsPageContainer extends Component {

  constructor(props){
    super(props);
    this.state = {
      numHiddenLayers: "none",
      hiddenLayerSizes: []
    }
  }

  range = (n) => !n || n === "0" ? [] : [...this.range(n-1), n-1];

  onNumHiddenLayersChange = (event) => {
    this.setState({
      numHiddenLayers: event.target.value,
      hiddenLayerSizes: this.range(event.target.value).map(i => "")
    });

  }

  onHiddenLayerSizeChange = (i) => {
    return (event) => {
      let newHiddenLayerSizes = [...this.state.hiddenLayerSizes];
      newHiddenLayerSizes[i] = parseInt(event.target.value);
      this.setState({hiddenLayerSizes: newHiddenLayerSizes});
    }
  }

  render() {
    return (
      <ChoosePlainNetworkDetailsPage
          numHiddenLayers={this.state.numHiddenLayers} 
          onNumHiddenLayersChange={this.onNumHiddenLayersChange} 
          hiddenLayerSizes={this.state.hiddenLayerSizes}
          onHiddenLayerSizeChange={this.onHiddenLayerSizeChange}
          submitNetworkDetails={() => this.props.submitNetworkDetails(this.state)}
      />
    )
  }
}