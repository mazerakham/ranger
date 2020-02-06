import React, { Component } from 'react';

import SessionPage from './SessionPage';

export default class SessionPageContainer extends Component {

  render() {
    return (
      <SessionPage sessionOptions={this.props.sessionOptions} />
    )
  }

}