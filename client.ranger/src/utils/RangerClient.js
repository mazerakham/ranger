

export default class RangerClient {

  constructor() {
    this.baseUrl = 'http://localhost:3001';
  }

  createNeuralNetwork = (hiddenLayerSize) => {
    return fetch(this.baseUrl + '/newNeuralNetwork', {
      method: 'post',
      body: JSON.stringify({hiddenLayerSize: hiddenLayerSize})
    }).then( response => response.json());
  }

  getTrainingHistory = () => {
    return fetch(this.baseUrl + '/trainingHistory', {
      method: 'get'
    }).then( response => response.json() );
  }

  getNeuralFunctionPlot = (neuralNetwork) => {
    return fetch(this.baseUrl + '/neuralFunctionPlot', {
      method: 'post',
      body: JSON.stringify({neuralNetwork: neuralNetwork})
    }).then( response => {
      return response.json();
    });
  }

}
