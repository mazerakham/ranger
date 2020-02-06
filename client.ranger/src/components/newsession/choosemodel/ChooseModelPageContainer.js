import React, { Component } from 'react';

import ChooseModelPage from './ChooseModelPage';

export default class ChooseModelPageContainer extends Component {

  selectPlain = () => {
    this.props.selectModel('plain');
  }

  selectRanger = () => {
    this.props.selectModel('ranger');
  }

  render() {
    return (
      <ChooseModelPage container={this} />
    )
  }
}