import React, { Component } from 'react';

import SVG from 'components/meta/SVG';

export default class Neuron extends Component {
  constructor(props) {
    super(props);
    this.coords = {w:1, h:1};
  }
  render() {
    return (
      <SVG parentCoords={this.props.coords} coords={this.coords}>
        <circle cx="0.5" cy="0.5" r="0.45" />
      </SVG>
    )
  }
}
