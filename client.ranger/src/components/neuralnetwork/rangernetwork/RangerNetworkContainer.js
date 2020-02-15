import React, { Component } from 'react';

import RangerNetwork from './RangerNetwork';
export default class RangerNetworkContainer extends Component {

  constructor(props){
    super(props);
    this.getNeuralNetworkInfo();
  }

  componentDidUpdate(prevProps, prevState) {
    this.getNeuralNetworkInfo();
  }

  getNeuralNetworkInfo = () => {
    if (!this.props.neuralNetwork) {
      return;
    }
    
    this.layers = this.props.rangerNetwork.layers;
    this.numLayers = this.layers.length;
    this.layerSizes = this.layers.map(layer => Object.keys(layer.neurons).length);
    this.allNeurons = {};

    // Enumerate the neurons within in each layer and globally.
    for (let layer of this.layers) {
      try {
        for (let [i, [uuid, neuron]] of enumerate(Object.entries(layer.neurons))) {
          layer.neurons[uuid].position = i;
          this.allNeurons[uuid] = neuron;
        }
      } catch (e) {
        console.log("Layer");
        console.log(layer);
        throw e;
      }
    }
  }

  displayNeuronInfo = (uuid) => {
    console.log("Received UUID " + uuid + "; passing neuron to parent:");
    console.log(this.allNeurons[uuid]);
    this.props.displayNeuronInfo(this.allNeurons[uuid]);
  }

  render() {
    return (
      <RangerNetwork
          layers={this.layers}
          numLayers={this.numLayers}
          rangerNetwork={this.props.neuralNetwork} 
          displayNeuronInfo={this.displayNeuronInfo}
      />
    )
  }
}