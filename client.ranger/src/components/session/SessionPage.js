import React, { Component } from 'react';

import ControlSectionContainer from  './elements/ControlSectionContainer';
import NeuralFunctionPlotContainer from  './elements/NeuralFunctionPlotContainer';
import NeuralNetworkPanel from  './elements/NeuralNetworkPanel';
import DesiredFunctionPlotContainer from  './elements/DesiredFunctionPlotContainer';


import './Session.css';

export default class SessionPage extends Component {

  render() {
    return (
      <div className="Session Container">
        <h1>Session</h1>
        <div className="Session Row">
          <NeuralNetworkPanel neuralNetwork={this.props.neuralNetwork} />
          <NeuralFunctionPlotContainer plot={this.props.plot} />
        </div>
        <div className="Session Row">
          <ControlSectionContainer performTrainingStep={this.props.performTrainingStep} />
          <DesiredFunctionPlotContainer plot={this.props.desiredPlot} />
        </div>
      </div>
    )
  }

}