package tensor;

import static tensor.DMatrixN.round;

import org.jblas.FloatMatrix;

public class FMatrix {
	
	private FloatMatrix m;

	/**
	 * Creates a 4x4 identity matrix.
	 */
	public FMatrix(int r, int c) {
		m = new FloatMatrix(r, c);
	}
	
	protected FMatrix(FloatMatrix m) {
		this.m = m;
	}

	/**
	 * Sets this matrix to the identity matrix.
	 */
	public static FMatrix identity(int size) {
		return new FMatrix(FloatMatrix.eye(size));
	}
	
	public static FMatrix rand(int r, int c, float range) {
		return new FMatrix(FloatMatrix.rand(r, c).sub(.5f).mul(2 * range));
	}
	
	public void setZero() {
		m = FloatMatrix.zeros(m.rows, m.columns);
	}
	
	public int rows() {
		return m.rows;
	}
	
	public int columns() {
		return m.columns;
	}
	
	public float get(int i, int j) {
		return m.get(i, j);
	}
	
	public void add(FMatrix other) {
		m = m.add(other.m);
	}
	
	/**
	 * Adds this matrix to another matrix.
	 *
	 * @param other
	 *            The other matrix
	 *
	 * @return Sum of this + other
	 */
	public FMatrix plus(FMatrix other) {
		return new FMatrix(m.add(other.m));
	}
	
	public void sub(FMatrix other) {
		m = m.sub(other.m);
	}

	/**
	 * Subtracts this matrix from another matrix.
	 *
	 * @param other
	 *            The other matrix
	 *
	 * @return Difference of this - other
	 */
	public FMatrix minus(FMatrix other) {
		return new FMatrix(m.sub(other.m));
	}

	/**
	 * Multiplies this matrix with a scalar.
	 *
	 * @param scalar
	 *            The scalar
	 *
	 * @return Scalar product of this * scalar
	 */
	public FMatrix multiply(float scalar) {
		return new FMatrix(m.mul(scalar));
	}

	/**
	 * Multiplies this matrix to a vector.
	 *
	 * @param vector
	 *            The vector
	 *
	 * @return Vector product of this * other
	 */
	public FVector multiply(FVector vector) {
		return new FVector(m.mmul(vector.v));
	}

	/**
	 * Multiplies this matrix to another matrix.
	 *
	 * @param other
	 *            The other matrix
	 *
	 * @return Matrix product of this * other
	 */
	public FMatrix multiply(FMatrix other) {
		return new FMatrix(m.mmul(other.m));
	}

	/**
	 * Transposes this matrix.
	 *
	 * @return Transposed matrix
	 */
	public FMatrix transpose() {
		return new FMatrix(m.transpose());
	}

	public String toString() {
		String ret = "";
		for (int i = 0; i < m.rows; i++) {
			for (int j = 0; j < m.columns; j++)
				ret += round(get(i, j)) + "\t";
			ret += "\n";
		}
		return ret;
	}
}
