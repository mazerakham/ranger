import React, {Component} from 'react';

import 'styles/App.css';

import TestPageContainer from 'components/test/TestPageContainer';
import HomePageContainer from 'components/home/HomePageContainer';
import DataExplorationPageContainer from 'components/dataexploration/DataExplorationPageContainer';

export default class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      currentPage: "home"
    };
  }

  loadPage = (pageName) => {
    this.setState({currentPage: pageName});
  }

  renderCurrentPage = () => {
    switch (this.state.currentPage) {
      case "test":
        return (<TestPageContainer />);
      case "home":
        return (<HomePageContainer loadPage={this.loadPage} />);
      case "dataExploration":
        return (<DataExplorationPageContainer />);
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
