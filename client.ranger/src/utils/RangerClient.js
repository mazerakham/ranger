

export default class RangerClient {

  constructor() {
    this.baseUrl = 'http://localhost:3001';
  }

  newNeuralNetwork = (datasetType, numHiddenLayers, hiddenLayerSizes) => {
    return fetch(this.baseUrl + '/newNeuralNetwork', {
      method: 'post',
      body: JSON.stringify({neuralNetworkSpecs: {
        datasetType: datasetType,
        numHiddenLayers: numHiddenLayers,
        hiddenLayerSizes: hiddenLayerSizes
      }})
    }).then(response => response.json());
  }

  neuralFunctionPlot = (datasetType, neuralNetwork) => {
    return fetch(this.baseUrl + '/neuralFunctionPlot', {
      method: 'post',
      body: JSON.stringify({
        datasetType: datasetType,
        neuralNetwork: neuralNetwork
      })
    }).then(response => response.json());
  }

  desiredPlot = (datasetType) => {
    return fetch(this.baseUrl + '/desiredPlot', {
      method: 'post',
      body: JSON.stringify({
        datasetType: datasetType
      })
    }).then(response => response.json());
  }

  train = (datasetType, neuralNetwork, batchSize, numSteps) => {
    return fetch(this.baseUrl + '/train', {
      method: 'post',
      body: JSON.stringify({
        datasetType: datasetType,
        neuralNetwork: neuralNetwork,
        batchSize: batchSize,
        numSteps: numSteps
      })
    }).then(response => response.json());
  }

}
