

export default class RangerClient {

  constructor() {
    this.baseUrl = 'http://localhost:3001';
  }

  createNeuralNetwork = async (hiddenLayerSize) => {
    const response = await fetch(this.baseUrl + '/newNeuralNetwork', {
      method: 'post',
      body: JSON.stringify({hiddenLayerSize: hiddenLayerSize})
    });
    const json = await response.json();
    console.log(json);
    return json;
  }

  getTrainingHistory = async () => {
    const response = await fetch(this.baseUrl + '/trainingHistory', {
      method: 'get'
    });
    const json = await response.json();
    console.log(json);
    return json;
  }

}
