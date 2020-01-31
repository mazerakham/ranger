package ranger.math;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.List;

import ox.Json;

public class Vector implements Cloneable {

  private final ArrayList<Double> entries = new ArrayList<>();

  public static Vector zeros(int size) {
    Vector ret = new Vector();
    for (int i = 0; i < size; i++) {
      ret.addEntry(0.0);
    }
    return ret;
  }

  public static Vector ones(int size) {
    Vector ret = new Vector();
    for (int i = 0; i < size; i++) {
      ret.addEntry(1.0);
    }
    return ret;
  }

  public static Vector basisVector(int n, int size) {
    Vector ret = new Vector();
    for (int i = 0; i < size; i++) {
      ret.addEntry(i == n ? 1.0 : 0.0);
    }
    return ret;
  }

  public Vector(List<Double> entries) {
    for (double entry : entries) {
      this.entries.add(entry);
    }
  }

  public Vector(double... entries) {
    for (double entry : entries) {
      addEntry(entry);
    }
  }

  /**
   * Multiply the vector by the scalar c and return the result. Does not mutate this vector.
   */
  public Vector scale(double c) {
    Vector ret = new Vector();
    for (int i = 0; i < size(); i++) {
      ret.addEntry(getEntry(i) * c);
    }
    return ret;
  }

  /**
   * Returns the result of adding this vector and that vector.
   */
  public Vector plus(Vector that) {
    Vector ret = new Vector();
    for (int i = 0; i < size(); i++) {
      ret.addEntry(this.getEntry(i) + that.getEntry(i));
    }
    return ret;
  }

  public double dot(Vector that) {
    checkState(this.size() == that.size(), String.format("this size: %d, that size: %d", this.size(), that.size()));
    double ret = 0;
    for (int i = 0; i < size(); i++) {
      ret += this.getEntry(i) * that.getEntry(i);
    }
    return ret;
  }

  public double distance(Vector that) {
    checkState(this.size() == that.size());
    return this.plus(that.scale(-1.0)).magnitude();
  }

  public double magnitude() {
    return Math.sqrt(this.dot(this));
  }

  public Vector addEntry(double d) {
    entries.add(d);
    return this;
  }

  public double getEntry(int i) {
    return entries.get(i);
  }

  public void setEntry(int i, double val) {
    entries.set(i, val);
  }

  public int size() {
    return entries.size();
  }

  public Json toJson() {
    return Json.array(this.entries);
  }

  @Override
  public String toString() {
    if (size() == 0) {
      return "[]";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < size(); i++) {
      sb.append(String.format("%.2f, ", getEntry(i)));
    }
    sb.delete(sb.length() - 2, sb.length());
    sb.append("]");
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Vector)) {
      return false;
    }
    Vector that = (Vector) o;
    if (this.size() != that.size()) {
      return false;
    }
    for (int i = 0; i < size(); i++) {
      if (this.getEntry(i) != that.getEntry(i)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Vector clone() {
    Vector ret = new Vector();
    for (double d : entries) {
      ret.addEntry(d);
    }
    return ret;
  }
}
