import React, { Component } from 'react';

import HomePage from './HomePage';

export default class HomePageContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {loadingStatus: "NONE"};
  }

  startNewSession = () => {
    this.props.app.loadPage("newSession");
  }

  loadSession = () => {
    alert("Loading session not yet supported.");
  }

  render() {
    return (
      <HomePage container={this} loadingStatus={this.state.loadingStatus}/>
    );
  }
}
