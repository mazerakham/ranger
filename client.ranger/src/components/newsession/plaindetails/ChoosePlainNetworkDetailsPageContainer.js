import React, { Component } from 'react';

import ChoosePlainNetworkDetailsPage from './ChoosePlainNetworkDetailsPage';

export default class ChoosePlainNetworkDetailsPageContainer extends Component {

  constructor(props){
    super(props);
    this.state = {
      numHiddenLayers: "none",
      layerSizes: []
    }
  }

  range = (n) => !n || n === "0" ? [] : [...this.range(n-1), n-1];

  onNumHiddenLayersChange = (event) => {
    console.log("Layer Change Event:", event);
    console.log(event.target.value);
    this.setState({
      numHiddenLayers: event.target.value,
      layerSizes: this.range(event.target.value).map(i => "")
    });

  }

  onLayerSizeChange = (i) => {
    return (event) => {
      let newLayerSizes = [...this.state.layerSizes];
      newLayerSizes[i] = parseInt(event.target.value);
      this.setState({layerSizes: newLayerSizes});
    }
  }

  render() {
    return (
      <ChoosePlainNetworkDetailsPage
          numHiddenLayers={this.state.numHiddenLayers} 
          onNumLayersChange={this.onNumHiddenLayersChange} 
          layerSizes={this.state.layerSizes}
          onLayerSizeChange={this.onLayerSizeChange}
          submitNetworkDetails={() => this.props.submitNetworkDetails(this.state)}
      />
    )
  }
}