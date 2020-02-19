import React, { Component } from 'react';

export default class Neuron extends Component {

  getInputPath = () => {
    const cx = this.props.coords.centerX;
    const cy = this.props.coords.centerY;
    const p1 = "" + (cx + 0.4) + "," + cy;
    const p2 = "" + (cx - 0.2) + "," + (cy - 0.2 * Math.sqrt(3));
    const p3 = "" + (cx - 0.2) + "," + (cy + 0.2 * Math.sqrt(3));
    return "M" + p1 + " L" + p2 + " L" + p3 + " Z";
  }

  getOutputPath = () => {
    const cx = this.props.coords.centerX;
    const cy = this.props.coords.centerY;
    const p1 = "" + (cx - 0.4) + "," + cy;
    const p2 = "" + (cx + 0.2) + "," + (cy - 0.2 * Math.sqrt(3));
    const p3 = "" + (cx + 0.2) + "," + (cy + 0.2 * Math.sqrt(3));
    return "M" + p1 + " L" + p2 + " L" + p3 + " Z";
  }

  getFill = () => {
    let s = this.props.s;
    let rgbVal = 255 * (1-s);
    return "rgb(" + rgbVal + "," + rgbVal + "," + rgbVal + ")";
  }

  getStyle = () => {
    return {
      stroke: "black",
      strokeWidth: "0.03",
      fill: this.getFill()
    }
  }

  render() {
    switch (this.props.type) {
      case "INPUT": return (
        <path d={this.getInputPath()} style={this.getStyle()} onClick={this.props.displayNeuronInfo} />
      );
      case "IDENTITY": return (
        <rect x={this.props.coords.centerX - 0.2} y={this.props.coords.centerY - 0.2} width={0.4} height={0.4} onClick={this.props.displayNeuronInfo} style={this.getStyle()} />
      );
      case "HIDDEN": return (
        <circle cx={this.props.coords.centerX} cy={this.props.coords.centerY} r={this.props.coords.radius} style={this.getStyle()} onClick={this.props.displayNeuronInfo} />
      );
      case "OUTPUT": return (
        <path d={this.getOutputPath()} style={this.getStyle()} onClick={this.props.displayNeuronInfo} />
      );
      default: throw new Error("neuron type " + this.props.type + " is unsupported.");
    }
  }
}