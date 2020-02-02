import React, { Component } from 'react';

export default class LoadingText extends Component {

  renderLoading = () => {
    return (
      <div className="LoadingText">Loading...</div>
    );
  }

  renderFinished = () => {
    return (
      <div className="LoadingText">Dataset Loaded. Redirecting to neural network setup!</div>
    )
  }

  render() {
    switch (this.props.loadingStatus) {
      case "NONE":
        return null;
      case "LOADING":
        return this.renderLoading();
      case "FINISHED":
        return this.renderFinished();
      default:
        console.log("Unknown state: " + this.props.loadingStatus);
        return null;
    }
  }
}
