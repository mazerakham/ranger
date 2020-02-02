import React, { Component } from 'react';

import HomePage from './HomePage';

export default class HomePageContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {loadingStatus: "NONE"};
  }

  loadDataset = () => {
    console.log("Home Page Container loading dataset");
    this.setState({loadingStatus:"LOADING"});
    this.loadingStatus = "LOADING";
    setTimeout(this.finishLoadingDataset, 250);
  };

  finishLoadingDataset = () => {
    console.log("Loading finished.");
    this.setState({loadingStatus:"FINISHED"});
    setTimeout(() => this.props.app.loadPage("dataExploration"), 250);
  }

  render() {
    return (
      <HomePage container={this} loadingStatus={this.state.loadingStatus}/>
    );
  }
}
