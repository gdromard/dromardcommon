/**
 * File : Matrix.java 11 févr. 08
 */

package net.dromard.common.matrix;

/**
 * A matrix of decimal numbers. Whenever talking about size of the matrix, we refer (n,m) as being n for height (rows) and m for width (columns).
 */
public final class Matrix {

    /**
     * The actual matrix data. Row x Column.It is adviced to use i or r for dows j or c for columns.
     */
    private double[][] data;

    /**
     * Creates a matrix using given dimensions.
     * @param r number of rows (height), non-null positive
     * @param c number of columns (width), non-null positive
     */
    public Matrix(final int r, final int c) {
        if (c < 1 || r < 1) {
            throw new IllegalArgumentException();
        }
        this.data = new double[r][c];
    }

    /**
     * Creates a new matrix using the same values as an example matrix.
     * @param m example matrix
     */
    public Matrix(final Matrix m) {
        this.data = cloneData(m.data);
    }

    /**
     * Creates a matrix from a bi-dimension array.
     * @param data cell content.
     */
    public Matrix(final double[][] data) {
        this.data = cloneData(data);
    }

    /**
     * Returns one cell value of the matrix.
     * @param i row, vertical axis coordinate, in [0 and {@link #getHeight()}[
     * @param j column, horizontal axis coordinate, in [0 and {@link #getWidth()}[
     * @return data of the cell
     */
    public double get(final int i, final int j) {
        if (i < 0 || i >= getHeight() || j < 0 || j >= getWidth()) {
            throw new IllegalArgumentException();
        }
        return data[i][j];
    }

    /**
     * Sets value of one cell of the matrix.
     * @param i row, vertical axis coordinate, in [0 and {@link #getHeight()}[
     * @param j column, horizontal axis coordinate, in [0 and {@link #getWidth()}[
     * @param val value to set
     */
    public void set(final int i, final int j, final double val) {
        if (i < 0 || i >= getHeight() || j < 0 || j >= getWidth()) {
            throw new IllegalArgumentException();
        }
        data[i][j] = val;
    }

    /**
     * @return width of the matrix.
     */
    public int getWidth() {
        return data[0].length;
    }

    /**
     * @return height of the matrix.
     */
    public int getHeight() {
        return data.length;
    }

    /**
     * Adds two matrix. The two matrix remains untouched, a third one is created holding the result.
     * @param m1 first term
     * @param m2 second term, requires same size as first matrix
     * @return result, will be same size as initial matrices
     */
    public static Matrix add(final Matrix m1, final Matrix m2) {
        if (m1.getWidth() != m2.getWidth() || m1.getHeight() != m2.getHeight()) {
            throw new IllegalArgumentException();
        }

        Matrix res = new Matrix(m1.getHeight(), m1.getWidth());
        for (int i = 0; i < m1.getHeight(); i++) {
            for (int j = 0; j < m1.getWidth(); j++) {
                double sum = m1.get(i, j) + m2.get(i, j);
                res.set(i, j, sum);
            }
        }
        return res;
    }

    /**
     * Multiplies two matrix. The two matrix remains untouched, a third one is created holding the result.
     * @param m1 first term
     * @param m2 second term, first matrix width is expected to be second matrix height.
     * @return result, size will be first matrix width (columns) by second matrix height (rows), i.e. Product of matrix A(n,m) by B(m,p) is C(n,p)
     * where Cij is scalar product of line Ai, by column Bj.
     */
    public static Matrix times(final Matrix m1, final Matrix m2) {
        if (m1.getWidth() != m2.getHeight()) {
            throw new IllegalArgumentException();
        }
        Matrix res = new Matrix(m1.getHeight(), m2.getWidth());
        for (int i = 0; i < res.getHeight(); i++) {
            for (int j = 0; j < res.getWidth(); j++) {
                double scal = Matrix.scalar(m1, i, m2, j);
                res.set(i, j, scal);
            }
        }
        return res;
    }

    /**
     * Scalar product of line i of matrix m1, by column j of matrix m2.
     * @param m1 first matrix
     * @param l1 line of first matrix
     * @param m2 second matrix (whose height is first matrix width)
     * @param c2 column of second matrix
     * @return scalar product
     */
    private static double scalar(final Matrix m1, final int l1, final Matrix m2, final int c2) {
        double scal = 0f;
        for (int k = 0; k < m1.getWidth(); k++) {
            scal += m1.get(l1, k) * m2.get(k, c2);
        }
        return scal;
    }

    /**
     * Copies a two dimension table to a new table with exact same dimension, and holding the exact same data.
     * @param data original to copy
     * @return copy result
     */
    private static double[][] cloneData(final double[][] data) {
        double[][] clone = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                clone[i][j] = data[i][j];
            }
        }
        return clone;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        final int prime = 31; // variable justt o satisfy checkstyle :/
        return prime * this.getWidth() * this.getHeight();
    }

    /**
     * Compare two instances for equality.
     * @param o matrix to compare against j
     * @return <code>true</code> if the two matrix are of exact same size and contains the exact same values.
     */
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Matrix mo = (Matrix) o;

        if (this.getHeight() != mo.getHeight() || this.getWidth() != mo.getWidth()) {
            return false;
        }

        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (this.data[i][j] != mo.data[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates a square matrix whose values are all 0 but diagonal which are set to 1. Maths call that kind of matrix Identity matrix, cause they are
     * neutral in matrix multiplication.
     * @param size rows and columns of the matrix (same value, since it's square matrix).
     * @return the identity matrix of given size.
     */
    public static Matrix createIdentityMatrix(final int size) {
        Matrix id = new Matrix(size, size);

        for (int i = 0; i < size; i++) {
            id.set(i, i, 1f);
        }

        return id;
    }
}
