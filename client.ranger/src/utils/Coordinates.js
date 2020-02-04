

export default class Coordinates {

  constructor() {
    // Do I need this?
  }

  fromChildCoords = ([x0, y0], childCoordsEmbedding, childCoords) => {
    const {x,y,w,h} = {...childCoordsEmbedding}
    const [xc, yc, wc, hc] = [childCoords.x || 0, childCoords.y || 0, childCoords.w, childCoords.h];

    return [
      x + (x0 - xc) * w / wc,
      y + (y0 - yc) * h / hc
    ]
  }

}
