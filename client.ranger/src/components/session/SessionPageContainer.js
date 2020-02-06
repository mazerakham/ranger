import React, { Component } from 'react';

import SessionPage from './SessionPage';

import RangerClient from 'utils/RangerClient';

export default class SessionPageContainer extends Component {

  constructor(props) {
    super(props);
    this.rangerClient = new RangerClient();
    this.state = {
      stage: "loading"
    }
  }

  componentDidMount() {
    this.rangerClient.createSession(this.props.sessionOptions).then(json => {
      console.log("Received response json:");
      console.log(json);
      this.setState({
        stage: "finished",
        neuralNetwork: json.neuralNetwork,
        session: json.session
      });
    })
  }

  render() {
    console.log(this.props.sessionOptions);
    switch (this.state.stage) {
      case "loading":
        return ( <div><h1>Creating Session.</h1></div>);
      case "finished":
        return ( 
          <SessionPage 
              sessionOptions={this.props.sessionOptions}
              neuralNetwork={this.state.neuralNetwork}
          /> 
        )
      default:
        throw new Error("Did not recognize stage " + this.state.stage);
    }
  }

}