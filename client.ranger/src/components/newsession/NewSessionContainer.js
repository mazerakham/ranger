import React, { Component } from 'react';

import ChooseDatasetPageContainer from './choosedataset/ChooseDatasetPageContainer';
import ChooseModelPageContainer from './choosemodel/ChooseModelPageContainer';
import ChoosePlainNetworkDetailsPageContainer from './plaindetails/ChoosePlainNetworkDetailsPageContainer';
import RangerNetworkDetailsPageContainer from './rangerdetails/RangerNetworkDetailsPageContainer';

import './NewSession.css';

export default class NewSessionContainer extends Component {

  constructor(props) {
    super(props);
    this.state = { stage: 'dataset' };
  }

  selectDataset = (datasetType) => {
    this.setState({
      datasetType: datasetType, 
      stage: 'model' 
    });
  }

  selectModel = (modelType) => {
    if (modelType === 'plain') {
      this.setState({
        modelType: 'plain',
        stage: 'plainDetails'
      });
    } else if (modelType === 'ranger') {
      this.setState({
        modelType: 'ranger',
        stage: 'rangerDetails'
      })
    }
  }

  selectNetworkDetails = (neuralNetworkSpecs) => {
    this.setState({
      neuralNetworkSpecs: neuralNetworkSpecs
    });
    let allOptions = {
      ...this.state, 
      neuralNetworkSpecs: neuralNetworkSpecs
    };
    this.props.app.startNewSession(allOptions);
  }

  render() {
    switch (this.state.stage) {
      case 'dataset':
        return ( <ChooseDatasetPageContainer selectDataset={this.selectDataset} /> );
      case 'model':
        return ( <ChooseModelPageContainer selectModel={this.selectModel} />);
      case 'plainDetails':
        return ( <ChoosePlainNetworkDetailsPageContainer submitNetworkDetails={this.selectNetworkDetails} /> );
      case 'rangerDetails':
        return ( <RangerNetworkDetailsPageContainer submitNetworkDetails={this.selectNetworkDetails} /> );
      default:
        throw new Error('Unknown page: ' + this.state.stage);
    }
  }

}
