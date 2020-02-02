import React, { Component } from 'react';

import 'styles/ModalBackdrop.css';

export default class ModalBackdrop extends Component {

  render() {
    return (
      <div
          className = "ModalBackdrop"
          onClick={this.props.hide}
      />
    );
  }
}
