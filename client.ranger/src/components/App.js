import React, {Component} from 'react';

import TestPageContainer from 'components/test/TestPageContainer';
import HomePageContainer from 'components/home/HomePageContainer';
import DataExplorationPageContainer from 'components/dataexploration/DataExplorationPageContainer';
import NewSessionContainer from 'components/newsession/NewSessionContainer';
import SessionPageContainer from 'components/session/SessionPageContainer';

import 'styles/App.css';

import RangerClient from 'utils/RangerClient';

export default class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      currentPage: "home",
      sessionOptions: {
        datasetType: 'xor',
        modelType: 'plain',
        numHiddenLayers: 2,
        layerSizes: [5,3]
      }
    };
  }

  loadPage = (pageName) => {
    this.setState({currentPage: pageName});
  }

  startNewSession = (sessionOptions) => {
    this.setState({
      sessionOptions: sessionOptions,
      currentPage: "session"
    })
  }

  renderCurrentPage = () => {
    switch (this.state.currentPage) {
      case "test":
        return (<TestPageContainer />);
      case "home":
        return (<HomePageContainer app={this} />);
      case "dataExploration":
        return (<DataExplorationPageContainer app={this}/>);
      case "newSession":
        return (<NewSessionContainer app={this}/>);
      case "session":
        return (<SessionPageContainer app={this} sessionOptions={this.state.sessionOptions} />);
      default:
        throw new Error("Unknown page: " + this.state.currentPage);
    }
  }

  render() {
    return (
      <div className="App">
        {this.renderCurrentPage()}
      </div>
    )
  }
}
