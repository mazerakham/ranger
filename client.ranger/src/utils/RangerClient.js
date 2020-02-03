

export default class RangerClient {

  createNeuralNetwork = async (hiddenLayerSize) => {
    const response = await fetch('http://localhost:3001/newNeuralNetwork', {
      method: 'post',
      body: JSON.stringify({hiddenLayerSize: hiddenLayerSize})
    });
    const json = await response.json();
    console.log(json);
    return json;
  }

}
