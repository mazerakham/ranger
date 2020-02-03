import React, { Component } from 'react';

import Modal from 'components/modal/Modal';

export default class NewNeuralNetworkModal extends Component {

  constructor(props) {
    super(props);
    this.state = {hiddenLayerSize: props.hiddenLayerSize};
  }

  render() {
    return (
      <Modal showing={this.props.showing} hide={this.props.hide}>
        <div>Choose neural network specifications.</div>
        <div>
          <div>Input Size: 2</div>
        </div>
        <div className="item">
          <div>Hidden Layer Size:</div>
          <input name="hiddenLayerSize"
              type="text"
              value={this.state.hiddenLayerSize}
              onChange={(e) => this.setState({hiddenLayerSize: e.target.value})}
          />
        </div>
        <div>
          <div>Output Size: 1</div>
        </div>
        <div className="buttons">
          <button onClick={() => this.props.onSubmit(this.state.hiddenLayerSize)}>Submit</button>
          <button onClick={this.props.hide}>Cancel</button>
        </div>
      </Modal>
    );
  }
}
