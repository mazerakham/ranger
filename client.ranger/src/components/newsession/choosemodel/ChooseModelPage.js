import React, { Component } from 'react';

import 'styles/NewSession.css';

export default class ChooseModelPage extends Component {

  render() {
    return (
      <div>
        <h1>Choose Model.</h1>
        <div className="choices">
          <div className="choice left" onClick={this.props.container.selectPlain}>Plain Neural Network</div>
          <div className="choice right" onClick={this.props.container.selectRanger}>Ranger Neural Network</div>
        </div>
      </div>

    )
  }
}