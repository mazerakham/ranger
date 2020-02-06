import React, { Component } from 'react';

import ChoosePlainNetworkDetailsPage from './ChoosePlainNetworkDetailsPage';

export default class ChoosePlainNetworkDetailsPageContainer extends Component {

  constructor(props){
    super(props);
    this.state = {
      numLayers: "none",
      layerSizes: []
    }
  }

  range = (n) => !n || n === "0" ? [] : [...this.range(n-1), n-1];

  onNumLayersChange = (event) => {
    this.setState({
      numLayers: event.target.value,
      layerSizes: this.range(event.target.value).map(i => "")
    });

  }

  onLayerSizeChange = (i) => {
    return (event) => {
      let newLayerSizes = [...this.state.layerSizes];
      newLayerSizes[i] = event.target.value;
      this.setState({layerSizes: newLayerSizes});
    }
  }

  render() {
    return (
      <ChoosePlainNetworkDetailsPage
          numLayers={this.state.numLayers} 
          onNumLayersChange={this.onNumLayersChange} 
          layerSizes={this.state.layerSizes}
          onLayerSizeChange={this.onLayerSizeChange}
          submitNetworkDetails={() => this.props.submitNetworkDetails(this.state)}
      />
    )
  }
}