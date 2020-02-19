import React, { Component } from 'react';

import ControlSectionContainer from  './elements/ControlSectionContainer';
import NeuralFunctionPlotContainer from  './elements/NeuralFunctionPlotContainer';
import ModelVisualizationContainer from  './elements/ModelVisualizationContainer';
import DesiredFunctionPlotContainer from  './elements/DesiredFunctionPlotContainer';
import ControlInputContainer from './elements/ControlInputContainer';
import NeuronDetailContainer from './elements/NeuronDetailContainer';


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
          <NeuronDetailContainer 
              neuron={this.props.neuron} 
          />
          <ModelVisualizationContainer 
              neuralNetwork={this.props.neuralNetwork} 
              modelType={this.props.sessionOptions.modelType} 
              displayNeuronInfo={this.props.displayNeuronInfo} 
          />
          <NeuralFunctionPlotContainer 
              plot={this.props.plot} 
          />
        </div>
        <div className="Session Row">
          <ControlInputContainer />
          <ControlSectionContainer 
              neuralNetwork={this.props.neuralNetwork} 
              updateNeuralNetwork={this.props.updateNeuralNetwork}
              updatePlot={this.props.updatePlot}
              modelType={this.props.sessionOptions.modelType}
              datasetType={this.props.sessionOptions.datasetType}
          />
          <DesiredFunctionPlotContainer 
              plot={this.props.desiredPlot} 
          />
        </div>
      </div>
    )
  }

}