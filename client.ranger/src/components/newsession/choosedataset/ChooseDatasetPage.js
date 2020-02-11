import React, { Component } from 'react';

export default class ChooseDatasetPage extends Component {

  render() {
    return (
      <div>
        <h1>Choose Your Dataset.</h1>
        <div className="dataset choices">
          <div className="choice left" onClick={this.props.container.selectXor}>X-OR</div>
          <div className="choice right" onClick={this.props.container.selectBullseye}>Bullseye</div>
        </div>
      </div>
    )
  }
}