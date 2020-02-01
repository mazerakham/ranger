import React, {Component} from 'react';

import 'styles/App.css';

import TestPageContainer from 'components/test/TestPageContainer';
export default class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      currentPage: "test"
    };
  }

  render() {
    return (
      <div className="App">
        <TestPageContainer />
      </div>
    )
  }
}
