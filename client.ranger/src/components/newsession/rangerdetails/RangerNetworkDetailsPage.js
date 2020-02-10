import React, { Component } from 'react';

export default class RangerNetworkDetailsPage extends Component {

  render() { 
    return (
      <div className="flexcolumn">
        <h1>Ranger Network Details</h1>
        <div>
          <span>a: </span>
          <input value={this.props.a} onChange={this.props.onAChange}/>
        </div>
        <button onClick={() => this.props.onSubmit(this.props.a)}>Submit</button>
      </div>
    )
  }
}