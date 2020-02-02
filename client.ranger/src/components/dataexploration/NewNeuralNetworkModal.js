import React, { Component } from 'react';

import Modal from 'components/modal/Modal';

export default class NewNeuralNetworkModal extends Component {

  render() {
    return (
      <Modal showing={this.props.showing} hide={this.props.hide}>
        <div>Hello World!</div>
        <div className="buttons">
          <button onClick={this.props.onSubmit}>Submit</button>
          <button onClick={this.props.hide}>Cancel</button>
        </div>
      </Modal>
    );
  }
}
