import React, { Component } from 'react';

import LoadingText from "./LoadingText";

import "styles/HomePage.css";

export default class HomePage extends Component {

  render() {
    return (
      <div>
        <h2>Ranger Client Homepage!</h2>
        <button onClick={this.props.container.loadDataset}>Load Dataset</button>
        <LoadingText loadingStatus={this.props.loadingStatus} />
      </div>
    );
  }
}
