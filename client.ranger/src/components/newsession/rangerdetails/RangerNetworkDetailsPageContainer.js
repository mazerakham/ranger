import React, { Component } from 'react';

import RangerNetworkDetailsPage from './RangerNetworkDetailsPage';

export default class RangerNetworkDetailsPageContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      a: 42
    }
  }

  onSubmit = (a) => {
    this.props.submitNetworkDetails({
      a: a
    });
  }

  onAChange = (a) => {
    this.setState({a: a});
  }

  render() {
    return (
      <RangerNetworkDetailsPage 
          onSubmit={this.onSubmit} 
          a={this.state.a}
          onAChange={this.onAChange}
      />
    )
  }

}