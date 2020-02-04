import React, { Component } from 'react';

import SVG from 'components/meta/SVG';

import NeuronConnection from './NeuronConnection';

export default class W1Layer extends Component {
  constructor(props) {
    super(props);
  }

  renderConnections = () => {
    const f = (a, b) => [].concat(...a.map(d => b.map(e => [].concat(d, e))));
    const cartesian = (a, b, ...c) => (b ? cartesian(f(a, b), ...c) : a);
    return cartesian([0,1],[0,1,2,3,4]).map(pair => this.renderConnection(pair[0], pair[1]));
  }

  renderConnection = (i,j) => {
    return <NeuronConnection coords={this.props.connectionCoordinates(i,j)} key={11*i + 37*j} />
  }

  render() {
    return (
      <React.Fragment>
        {this.renderConnections()}
      </React.Fragment>
    );
  }
}
