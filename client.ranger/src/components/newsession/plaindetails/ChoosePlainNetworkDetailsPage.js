import React, { Component } from 'react';

import { bRange } from 'utils/Numbers';

export default class ChoosePlainNetworkDetailsPage extends Component {

  renderHiddenLayerSizes = () => {
    if (this.props.numHiddenLayers === 'none') {
      return null;
    }

    return bRange(parseInt(this.props.numHiddenLayers)).map(i => {
      return (
        <div className="layer-size" key={i}>
          Layer {i+1} Size:
          <input type="text" value={this.props.hiddenLayerSizes[i]} onChange={this.props.onHiddenLayerSizeChange(i)}/>
        </div>
      )
    });
  }

  renderOptions() {
    return bRange(6).map(i => {
      return (
        <option value={i} key={i}>{i}</option>
      )
    });
  }

  render() {
    return (
      <div className="ChoosePlainNeuralNetworkDetails Page">
        <h1>Plain Network Details</h1>
        <div>
        <p>Number of Hidden Layers:</p>
          <select defaultValue="none" className="numHiddenLayers" type="select" onChange={this.props.onNumHiddenLayersChange}>
            <option value="none" disabled>Select Number of Hidden Layers.</option>
            { this.renderOptions() }
          </select>
        </div>
        { this.renderHiddenLayerSizes() }
        <div>
          <button onClick={this.props.submitNetworkDetails}>Submit</button>
        </div>
      </div>
    )
  }
}