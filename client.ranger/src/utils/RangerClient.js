

export default class RangerClient {

  constructor() {
    this.baseUrl = 'http://localhost:3001';
  }

  newNeuralNetwork = (modelType, specs) => {
    return fetch(this.baseUrl + '/newNeuralNetwork', {
      method: 'post',
      body: JSON.stringify({
        modelType: modelType,
        neuralNetworkSpecs: specs
      })
    }).then(response => response.json());
  }

  neuralFunctionPlot = (datasetType, modelType, neuralNetwork) => {
    return fetch(this.baseUrl + '/neuralFunctionPlot', {
      method: 'post',
      body: JSON.stringify({
        datasetType: datasetType,
        modelType: modelType,
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

  train = (datasetType, neuralNetwork, batchSize, numSteps, learningRate) => {
    console.log(datasetType, neuralNetwork, batchSize, numSteps, learningRate);
    return fetch(this.baseUrl + '/train', {
      method: 'post',
      body: JSON.stringify({
        datasetType: datasetType,
        neuralNetwork: neuralNetwork,
        batchSize: batchSize,
        numSteps: numSteps,
        learningRate: learningRate
      })
    }).then(response => response.json());
  }

}
