import React, { Component } from 'react';

import HomePage from './HomePage';

export default class HomePageContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {loadingStatus: "NONE"};
  }

  startNewSession = () => {
    console.log("Starting new session.");
    this.props.app.loadPage("newSession");
  }

  loadSession = () => {
    console.log("Loading a saved session is not yet supported.");
  }

  render() {
    return (
      <HomePage container={this} loadingStatus={this.state.loadingStatus}/>
    );
  }
}
