import React, { Component } from 'react';

import ModalBackdrop from 'components/modal/ModalBackdrop';
import "styles/Modal.css";

export default class Modal extends Component {

  render() {
    return (
      this.props.showing && (
        <React.Fragment>
          <ModalBackdrop hide={this.props.hide}/>
          <div className="Modal">
            {this.props.children}
          </div>
        </React.Fragment>
      )
    )
  }
}
