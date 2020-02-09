package ranger.nn.plot;

import ranger.math.Vector;

public class Window {

  private double xMin, xMax, yMin, yMax;

  public Window(double xMin, double xMax, double yMin, double yMax) {
    this.xMin = xMin;
    this.xMax = xMax;
    this.yMin = yMin;
    this.yMax = yMax;
  }

  public Vector getPoint(int i, int j, int resolution) {
    return new Vector(
        xMin + (xMax - xMin) * i / resolution,
        yMin + (yMax - yMin) * j / resolution);
  }
}