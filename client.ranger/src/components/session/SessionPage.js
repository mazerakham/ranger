import React, { Component } from 'react';

import ControlSectionContainer from  './elements/ControlSectionContainer';
import NeuralFunctionPlotContainer from  './elements/NeuralFunctionPlotContainer';
import ModelVisualizationContainer from  './elements/ModelVisualizationContainer';
import DesiredFunctionPlotContainer from  './elements/DesiredFunctionPlotContainer';


import './Session.css';

export default class SessionPage extends Component {

  render() {
    return (
      <div className="Session Container">
        <div className="Header">
          <button onClick={this.props.backToHome}>Back to Home</button>
          <h1>Session</h1>
        </div>
        <div className="Session Row">
          <ModelVisualizationContainer neuralNetwork={this.props.neuralNetwork} modelType={this.props.sessionOptions.modelType} />
          <NeuralFunctionPlotContainer plot={this.props.plot} />
        </div>
        <div className="Session Row">
          <ControlSectionContainer performTrainingStep={this.props.performTrainingStep} modelType={this.props.sessionOptions.modelType} />
          <DesiredFunctionPlotContainer plot={this.props.desiredPlot} />
        </div>
      </div>
    )
  }

}