import React, { Component } from 'react';

import { clip } from 'utils/Numbers';

export default class NeuronConnection extends Component {

  getStroke = () => {
    let rVal = Math.round(clip(0, 255, -255 * this.props.weight));
    let bVal = Math.round(clip(0, 255, 255 * this.props.weight));
    return "rgb(" + rVal + ",0," + bVal + ")";
  }

  render () {
    return (
      <line 
          x1={this.props.coords.x1} 
          y1={this.props.coords.y1} 
          x2={this.props.coords.x2} 
          y2={this.props.coords.y2} 
          style={{
            stroke:this.getStroke(), 
            strokeWidth:"0.05"
          }}
      />
    )
  }
}