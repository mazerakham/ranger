import React, { Component } from 'react';

export default class SVG extends Component {

  constructor(props) {
    super(props);
  }

  getViewbox = () => {
    return "0 0 " + this.props.coords.w + " " + this.props.coords.h;
  }

  static rootCoords = () => {
    return {
      x: 0,
      y: 0,
      w: "100%",
      h: "100%"
    }
  }

  render() {
    return (
      <svg
          className={this.props.className}
          x={this.props.parentCoords.x}
          y={this.props.parentCoords.y}
          width={this.props.parentCoords.w}
          height={this.props.parentCoords.h}
          viewBox={this.getViewbox()}
          style={{preserveAspectRatio:"xMidYMid meet"}}
      >
        {this.props.children}
      </svg>
    )
  }
}
