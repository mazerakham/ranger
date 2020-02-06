import React, { Component } from 'react';

import ChooseDatasetPageContainer from './choosedataset/ChooseDatasetPageContainer';
import ChooseModelPageContainer from './choosemodel/ChooseModelPageContainer';
import ChoosePlainNetworkDetailsPageContainer from './plaindetails/ChoosePlainNetworkDetailsPageContainer';

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
    this.setState({
      modelType: modelType,
    });
    if (modelType === 'plain') {
      this.setState({
        stage: 'plainDetails'
      });
    } else {
      throw new Error('Ranger network is not yet supported.');
    }
  }

  render() {
    switch (this.state.stage) {
      case 'dataset':
        return ( <ChooseDatasetPageContainer selectDataset={this.selectDataset} /> );
      case 'model':
        return ( <ChooseModelPageContainer selectModel={this.selectModel} />);
      case 'plainDetails':
        return ( <ChoosePlainNetworkDetailsPageContainer selectNetworkDetails={this.selectNetworkDetails} /> );
      default:
        throw new Error('Unknown page: ' + this.state.stage);
    }
  }

}
