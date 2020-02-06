import React, { Component } from 'react';

export default class ChoosePlainNetworkDetailsPage extends Component {

  range = (n) => !n || n === "0" ? [] : [...this.range(n-1), n-1];

  renderLayerSizes = () => {
    if (this.props.numLayers === 'none') {
      return null;
    }

    return this.range(parseInt(this.props.numLayers)).map(i => (
      <div className="layer-size" key={i}>
        Layer {i+1} Size:
        <input type="text" value={this.props.layerSizes[i]} onChange={this.props.onLayerSizeChange(i)}/>
      </div>
    ));
  }

  render() {
    return (
      <div className="ChoosePlainNeuralNetworkDetails Page">
        <h1>Plain Network Details</h1>
        <div>
        <p>Number of Layers:</p>
          <select defaultValue="none" className="numHiddenLayers" type="select" onChange={this.props.onNumLayersChange}>
            <option value="none" disabled>Select Number of Hidden Layers.</option>
            <option value={0}>0</option>
            <option value={1}>1</option>
            <option value={2}>2</option>
            <option value={3}>3</option>
          </select>
        </div>
        { this.renderLayerSizes() }
        <div>
          <button onClick={this.props.submitNetworkDetails}>Submit</button>
        </div>
      </div>
    )
  }
}