package tensor;

import java.nio.DoubleBuffer;

import static tensor.DMatrixN.round;

public class DMatrix2 {
	
	private double m00, m01;
	private double m10, m11;

	/**
	 * Creates a 4x4 identity matrix.
	 */
	public DMatrix2() {
		setIdentity();
	}

	/**
	 * Creates a 2x2 matrix with specified columns.
	 *
	 * @param col1
	 *            Vector with values of the first column
	 * @param col2
	 *            Vector with values of the second column
	 */
	public DMatrix2(DVector2 col1, DVector2 col2) {
		m00 = col1.x();
		m10 = col1.y();

		m01 = col2.x();
		m11 = col2.y();
	}

	/**
	 * Sets this matrix to the identity matrix.
	 */
	public final void setIdentity() {
		m00 = 1d;
		m11 = 1d;

		m01 = 0d;
		m10 = 0d;
	}

	/**
	 * Adds this matrix to another matrix.
	 *
	 * @param other
	 *            The other matrix
	 *
	 * @return Sum of this + other
	 */
	public DMatrix2 add(DMatrix2 other) {
		DMatrix2 result = new DMatrix2();

		result.m00 = this.m00 + other.m00;
		result.m10 = this.m10 + other.m10;

		result.m01 = this.m01 + other.m01;
		result.m11 = this.m11 + other.m11;

		return result;
	}

	/**
	 * Negates this matrix.
	 *
	 * @return Negated matrix
	 */
	public DMatrix2 negate() {
		return multiply(-1d);
	}

	/**
	 * Subtracts this matrix from another matrix.
	 *
	 * @param other
	 *            The other matrix
	 *
	 * @return Difference of this - other
	 */
	public DMatrix2 subtract(DMatrix2 other) {
		return this.add(other.negate());
	}

	/**
	 * Multiplies this matrix with a scalar.
	 *
	 * @param scalar
	 *            The scalar
	 *
	 * @return Scalar product of this * scalar
	 */
	public DMatrix2 multiply(double scalar) {
		DMatrix2 result = new DMatrix2();

		result.m00 = this.m00 * scalar;
		result.m10 = this.m10 * scalar;

		result.m01 = this.m01 * scalar;
		result.m11 = this.m11 * scalar;

		return result;
	}

	/**
	 * Multiplies this matrix to a vector.
	 *
	 * @param vector
	 *            The vector
	 *
	 * @return Vector product of this * other
	 */
	public DVector2 multiply(DVector2 vector) {
		double x = this.m00 * vector.x() + this.m01 * vector.y();
		double y = this.m10 * vector.x() + this.m11 * vector.y();
		return new DVector2(x, y);
	}

	/**
	 * Multiplies this matrix to another matrix.
	 *
	 * @param other
	 *            The other matrix
	 *
	 * @return Matrix product of this * other
	 */
	public DMatrix2 multiply(DMatrix2 other) {
		DMatrix2 result = new DMatrix2();

		result.m00 = this.m00 * other.m00 + this.m01 * other.m10;
		result.m10 = this.m10 * other.m00 + this.m11 * other.m10;

		result.m01 = this.m00 * other.m01 + this.m01 * other.m11;
		result.m11 = this.m10 * other.m01 + this.m11 * other.m11;

		return result;
	}

	/**
	 * Transposes this matrix.
	 *
	 * @return Transposed matrix
	 */
	public DMatrix2 transpose() {
		DMatrix2 result = new DMatrix2();

		result.m00 = this.m00;
		result.m10 = this.m01;

		result.m01 = this.m10;
		result.m11 = this.m11;

		return result;
	}

	/**
	 * Stores the matrix in a given Buffer.
	 *
	 * @param buffer
	 *            The buffer to store the matrix data
	 */
	public void toBuffer(DoubleBuffer buffer) {
		buffer.put(m00).put(m10);
		buffer.put(m01).put(m11);
		buffer.flip();
	}

	/**
	 * Creates a scaling matrix. Similar to <code>glScale(x, y, z)</code>.
	 *
	 * @param x
	 *            Scale factor along the x coordinate
	 * @param y
	 *            Scale factor along the y coordinate
	 * @param z
	 *            Scale factor along the z coordinate
	 *
	 * @return Scaling matrix
	 */
	public static DMatrix2 scale(double x, double y) {
		DMatrix2 scaling = new DMatrix2();

		scaling.m00 = x;
		scaling.m11 = y;

		return scaling;
	}
	
	public static DMatrix2 rotate(double angle) {
		DMatrix2 rotate = new DMatrix2();
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		
		rotate.m00 = c;
		rotate.m01 = -s;
		rotate.m10 = s;
		rotate.m11 = c;
		
		return rotate;
	}
	
	public String toString() {
		return round(m00) + "\t" + round(m01) + "\n"
			 + round(m10) + "\t" + round(m11);
	}

}
