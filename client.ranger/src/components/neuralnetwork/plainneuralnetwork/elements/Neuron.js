import React, { Component } from 'react';

export default class Neuron extends Component {

  render() {
    return (
      <circle cx={this.props.coords.centerX} cy={this.props.coords.centerY} r={this.props.coords.radius} />
    )
  }

}