import React, { Component } from 'react';

import RangerNetwork from './RangerNetwork';
export default class RangerNetworkContainer extends Component {

  render() {
    return (
      <RangerNetwork rangerNetwork={this.props.neuralNetwork} />
    )
  }
}