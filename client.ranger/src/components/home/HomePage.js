import React, { Component } from 'react';

import LoadingText from "./LoadingText";

import "./HomePage.css";

export default class HomePage extends Component {


  render() {
    return (
      <div>
        <h2>Ranger Client Home</h2>
        <button onClick={this.props.container.startNewSession}>Start New Session</button>
      <button onClick={this.props.container.loadSession}>Load Saved Session</button>
      </div>
    );
  }
}
