import React, { Component } from 'react';

export default class NeuronDetail extends Component {

  renderDendrites = () => {
    if (!this.props.neuron.dendrites) {
      return null;
    }
    return (
      <span>
        {
          Object.entries(this.props.neuron.dendrites).map(([uuid, weight]) => (<span key={Math.random()}>{uuid.substr(0,3) + ": " + weight.toFixed(2) + ", "}</span>))
        }
      </span>
    )
  }

  renderBias = () => {
    return (
      <span>{this.props.neuron.bias.toFixed(2)} </span>
    )
  }

  renderDendriteStimulus = () => {
    if (!this.props.neuron.activity.dendriteStimulus) {
      return null;
    } 
    return (
      <span>
        {
          Object.entries(this.props.neuron.activity.dendriteStimulus).map(([uuid, [value, strength]]) => (
            <span key={Math.random()}>{uuid.substr(0,3) + ": [" + value.toFixed(2) + ", " + strength.toFixed(2) + "], "}</span>
          ))
        }
      </span>
    )
  }

  renderAxonActivation = () => {
    if (!this.props.neuron.activity.activation) {
      return null;
    } 
    let [value, strength] = this.props.neuron.activity.activation;
    return (
      <span>
        {"[" + value.toFixed(2) + ", " + strength.toFixed(2) + "]"}
      </span>
    )
  }

  renderAxonSignal = () => {
    if (!this.props.neuron.activity.axonSignal) {
      return null;
    }
    return (
      <span>
        {this.props.neuron.activity.axonSignal.toFixed(2)}
      </span>
    )
  }

  renderDendriteSignal = () => {
    if (!this.props.neuron.activity.dendriteSignal) {
      return null;
    }
    return (
      <span>
        {
          Object.entries(this.props.neuron.activity.dendriteSignal).map(([uuid, value]) => (
            <span key={Math.random()}>
              {uuid.substr(0,3) + ": " + value.toFixed(2) + ", "}
            </span>
          ))
        }
      </span>
    )
  }

  renderNeuronInfo = () => {
    return (
      <div>
        <div>UUID: {this.props.neuron.uuid.substr(0,3)}</div>
        <div>State-Value: {this.props.neuron.s.toFixed(2)}</div>
        <div>Dendrites: { this.renderDendrites() }</div>
        <div>Bias: { this.renderBias() } </div>
        <div>Dendrite Stimulus: { this.renderDendriteStimulus() } </div>
        <div>Axon Activation: { this.renderAxonActivation() } </div>
        <div>Axon Signal: {this.renderAxonSignal()} </div>
        <div>Dendrite Signal: { this.renderDendriteSignal() } </div>
      </div>
    )
  }

  maybeRenderNeuronInfo = () => {
    if (this.props.neuron) {
      return this.renderNeuronInfo();
    } else {
      return (
        <div>No neuron selected.</div>
      )
    }
  }

  render() {
    return (
      <div className="Session Panel NeuronDetail">
        {this.maybeRenderNeuronInfo()}
      </div>
    )
  }
}