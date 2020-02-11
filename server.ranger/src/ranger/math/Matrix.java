package ranger.math;

import static com.google.common.base.Preconditions.checkState;
import static ox.util.Functions.sum;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Deque;
import java.util.function.BiFunction;

import com.google.common.collect.Queues;

import ox.Json;

public class Matrix implements Cloneable {

  public ArrayList<Vector> rows = new ArrayList<>();

  public static Matrix zeros(int height, int width) {
    Matrix ret = new Matrix();
    for (int i = 0; i < height; i++) {
      ret.addRowVector(Vector.zeros(width));
    }
    return ret;
  }

  public static Matrix identity(int n) {
    Matrix ret = new Matrix();
    for (int i = 0; i < n; i++) {
      ret.addRowVector(Vector.basisVector(i, n));
    }
    return ret;
  }

  public static Matrix fromFunction(int height, int width, BiFunction<Integer, Integer, Double> function) {
    Matrix ret = Matrix.zeros(height, width);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ret.setEntry(i, j, function.apply(i, j));
      }
    }
    return ret;
  }

  /**
   * Returns a matrix of the given width consisting of copies of the column.
   */
  public static Matrix columns(Vector column, int width) {
    Matrix ret = new Matrix();
    for (int i = 0; i < width; i++) {
      ret.addColumnVector(column);
    }
    return ret;
  }

  public Matrix(Vector... rows) {
    for (Vector rowVector : rows) {
      this.rows.add(rowVector);
    }
  }

  public static Matrix fromJson(Json json) {
    if (json == null) {
      return null;
    }

    Matrix ret = new Matrix();
    json.asJsonArray().forEach(rowJson -> {
      ret.addRowVector(Vector.fromJson(rowJson));
    });

    return ret;
  }

  /**
   * Add a row to this matrix.
   */
  public Matrix addRowVector(Vector v) {
    checkState(rows.isEmpty() || rows.get(0).size() == v.size());
    rows.add(v.clone());
    return this;
  }

  /**
   * Add a column to this matrix.
   */
  public Matrix addColumnVector(Vector v) {
    checkState(v.size() == height() || height() == 0);
    if (height() == 0) {
      for (int i = 0; i < v.size(); i++) {
        rows.add(new Vector());
      }
    }
    for (int i = 0; i < height(); i++) {
      rows.get(i).addEntry(v.getEntry(i));
    }
    return this;
  }

  /**
   * Returns the result of adding matrices.
   */
  public Matrix plus(Matrix that) {
    checkState(this.height() == that.height() && this.width() == that.width());
    Matrix ret = new Matrix();
    for (int i = 0; i < height(); i++) {
      ret.addRowVector(this.getRow(i).plus(that.getRow(i)));
    }
    return ret;
  }

  public Matrix scale(double d) {
    Matrix ret = new Matrix();
    for (int i = 0; i < height(); i++) {
      ret.addRowVector(this.getRow(i).scale(d));
    }
    return ret;
  }

  /**
   * Returns the result of augmenting the matrix on the right by the argument (heights must match).
   */
  public Matrix augment(Matrix right) {
    checkState(this.height() == right.height());
    Matrix ret = this.clone();
    for (int j = 0; j < right.width(); j++) {
      ret.addColumnVector(right.getCol(j));
    }
    return ret;
  }

  /**
   * Returns the result of transposing the matrix.
   */
  public Matrix transpose() {
    Matrix ret = Matrix.zeros(width(), height());
    for (int i = 0; i < height(); i++) {
      for (int j = 0; j < width(); j++) {
        ret.setEntry(j, i, getEntry(i, j));
      }
    }
    return ret;
  }

  /**
   * Returns the result Av of multiplying this matrix A by the vector v viewed as a column vector.
   */
  public Vector multiply(Vector v) {
    Vector ret = new Vector();
    for (int i = 0; i < height(); i++) {
      ret.addEntry(rows.get(i).dot(v));
    }
    return ret;
  }

  /**
   * Matrix multiplication this . that
   */
  public Matrix multiply(Matrix that) {
    checkState(this.width() == that.height());
    int m = this.height(), n = that.width();
    Matrix ret = Matrix.zeros(this.height(), that.width());
    Vector col;
    for (int j = 0; j < n; j++) {
      col = that.getCol(j);
      for (int i = 0; i < m; i++) {
        ret.setEntry(i, j, this.getRow(i).dot(col));
      }
    }
    return ret;
  }

  private final double EPSILON = 0.00001;

  private boolean isNearlyZero(double d) {
    return Math.abs(d) < EPSILON;
  }

  /**
   * Row-reduce to echelon form. Mutates the matrix.
   */
  private Matrix rref() {
    Matrix ret = this.clone();
    int pivotI = 0;
    int pivotJ = 0;
    int m = height(), n = width();
    Deque<Integer> pivotColumns = Queues.newArrayDeque();
    while (pivotI < m && pivotJ < n) {
      if (!ret.pivot(pivotI, pivotJ)) {
        pivotJ++;
      } else {
        ret.eliminateBelow(pivotI, pivotJ);
        pivotI++;
        pivotColumns.add(pivotJ++);
      }
    }
    while (!pivotColumns.isEmpty()) {
      pivotI--;
      pivotJ = pivotColumns.removeLast();
      ret.normalizeRow(pivotI, pivotJ);
      ret.eliminateAbove(pivotI, pivotJ);
    }
    return ret;
  }

  /**
   * Returns the inverse of this matrix.
   */
  public Matrix inverse() {
    checkState(this.width() == this.height());
    int n = width();
    Matrix mat = this.clone();
    mat = mat.augment(Matrix.identity(n));
    mat = mat.rref();
    Matrix ret = Matrix.zeros(n, n);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        ret.setEntry(i, j, mat.getEntry(i, j + n));
      }
    }
    return ret;
  }

  public double getEntry(int i, int j) {
    return rows.get(i).getEntry(j);
  }

  public void setEntry(int i, int j, double val) {
    rows.get(i).setEntry(j, val);
  }

  /**
   * Returns a copy of row i.
   */
  public Vector getRow(int i) {
    return rows.get(i).clone();
  }

  /**
   * Returns a copy of column j.
   */
  public Vector getCol(int j) {
    Vector ret = new Vector();
    for (int i = 0; i < height(); i++) {
      ret.addEntry(getRow(i).getEntry(j));
    }
    return ret;
  }

  public int width() {
    return rows.get(0).size();
  }

  public int height() {
    return rows.size();
  }

  public double frobeniusNorm() {
    return Math.sqrt(sum(this.rows, rv -> rv.dot(rv)));
  }

  /**
   * Attempt to switch row I with a row beneath it so as to put a non-zero entry at (I,J). Return false if this is not
   * possible, i.e. all entries are nearly zero.
   */
  private boolean pivot(int pivotI, int pivotJ) {
    for (int i = pivotI; i < height(); i++) {
      if (!isNearlyZero(getEntry(i, pivotJ))) {
        Vector pivotRow = rows.get(pivotI);
        rows.set(pivotI, rows.get(i));
        rows.set(i, pivotRow);
        return true;
      }
    }
    return false;
  }

  private void eliminateBelow(int pivotI, int pivotJ) {
    double pivotValue = getEntry(pivotI, pivotJ);
    for (int i = pivotI + 1; i < height(); i++) {
      double toEliminate = getEntry(i, pivotJ);
      rows.set(i, rows.get(i).plus(rows.get(pivotI).scale(-toEliminate / pivotValue)));
    }
  }

  private void normalizeRow(int pivotI, int pivotJ) {
    double val = getEntry(pivotI, pivotJ);
    rows.set(pivotI, rows.get(pivotI).scale(1.0 / val));
  }

  private void eliminateAbove(int pivotI, int pivotJ) {
    for (int i = pivotI - 1; i >= 0; i--) {
      double toEliminate = getEntry(i, pivotJ);
      rows.set(i, rows.get(i).plus(rows.get(pivotI).scale(-toEliminate)));
    }
  }

  @Override
  public Matrix clone() {
    Matrix ret = new Matrix();
    for (Vector row : rows) {
      ret.addRowVector(row.clone());
    }
    return ret;
  }

  private final DecimalFormat df = new DecimalFormat("#.##");

  public Json toJson() {
    return Json.array(rows, Vector::toJson);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < height(); i++) {
      sb.append(getRow(i) + "\n");
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    checkState(o instanceof Matrix);
    Matrix that = (Matrix) o;
    if (this.height() != that.height() || this.width() != that.width()) {
      return false;
    }
    for (int i = 0; i < height(); i++) {
      if (!this.getRow(i).equals(that.getRow(i))) {
        return false;
      }
    }
    return true;
  }
}
