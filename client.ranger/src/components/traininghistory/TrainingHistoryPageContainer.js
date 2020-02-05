import React, { Component } from 'react';

import TrainingHistoryPage from './TrainingHistoryPage';

import RangerClient from 'utils/RangerClient';

export default class TrainingHistoryPageContainer extends Component {

  constructor(props) {
    super(props);
    this.history = new RangerClient().getTrainingHistory();
  }

  render() {
    return (
      <TrainingHistoryPage history={this.history} neuralNetwork={this.history[0]}/>
    )
  }
}
