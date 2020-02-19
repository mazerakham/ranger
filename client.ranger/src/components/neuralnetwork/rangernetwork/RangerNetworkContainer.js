import React, { Component } from 'react';

import RangerNetwork from './RangerNetwork';

import { enumerate } from 'utils/Numbers';

export default class RangerNetworkContainer extends Component {

  constructor(props){
    super(props);
    this.getNeuralNetworkInfo();
  }

  componentDidUpdate(oldProps, oldState) {
    this.getNeuralNetworkInfo();
  }

  getNeuralNetworkInfo = () => {
    this.layers = this.props.neuralNetwork.layers;
    this.numLayers = this.layers.length;
    this.layerSizes = this.layers.map(layer => Object.keys(layer.neurons).length);
    this.allNeurons = {};

    // Enumerate the neurons within in each layer and globally.
    for (let [l,layer] of enumerate(this.layers)) {
      for (let [i, [uuid, neuron]] of enumerate(Object.entries(layer.neurons))) {
        layer.neurons[uuid].position = i;
        this.allNeurons[l + uuid] = neuron;
      }
    }
  }

  displayNeuronInfo = (uuid) => {
    this.props.displayNeuronInfo(this.allNeurons[uuid]);
  }

  render() {
    return (
      <RangerNetwork
          layers={this.layers}
          numLayers={this.numLayers}
          layerSizes={this.layerSizes}
          rangerNetwork={this.props.neuralNetwork} 
          displayNeuronInfo={this.displayNeuronInfo}
      />
    )
  }
}