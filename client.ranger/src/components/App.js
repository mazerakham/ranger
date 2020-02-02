import React, {Component} from 'react';

import 'styles/App.css';

import TestPageContainer from 'components/test/TestPageContainer';
import HomePageContainer from 'components/home/HomePageContainer';

export default class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      currentPage: "home"
    };
  }

  renderCurrentPage = () => {
    switch (this.state.currentPage) {
      case "test":
        return (<TestPageContainer />);
      case "home":
        return (<HomePageContainer />);
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
