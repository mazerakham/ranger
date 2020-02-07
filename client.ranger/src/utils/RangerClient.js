

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

  getNeuralFunctionPlot = (session, trainingStep) => {
    return fetch(this.baseUrl + '/neuralFunctionPlot', {
      method: 'post',
      body: JSON.stringify({
        session: session,
        trainingStep: trainingStep
      })
    }).then( response => {
      return response.json();
    });
  }

  train = () => {
    throw new Error("Not yet implemented.");
  }

}
