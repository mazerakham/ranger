import React, { Component } from 'react';

import ChooseDatasetPage from './ChooseDatasetPage';

export default class ChooseDatasetPageContainer extends Component {

  constructor(props) {
    super(props);
  }

  selectXor = () => {
    this.props.selectDataset("xor");
  }

  selectBullseye = () => {
    this.props.selectDataset("bullseye");
  }

  render() {
    return <ChooseDatasetPage container={this} />
  }
}