package tensor;

import java.nio.DoubleBuffer;

public class Matrixd {

	private double m00, m01, m02;
	private double m10, m11, m12;
	private double m20, m21, m22;

	/**
	 * Creates a 4x4 identity matrix.
	 */
	public Matrixd() {
		setIdentity();
	}

	/**
	 * Creates a 4x4 matrix with specified columns.
	 *
	 * @param col1
	 *            Vector with values of the first column
	 * @param col2
	 *            Vector with values of the second column
	 * @param col3
	 *            Vector with values of the third column
	 * @param col4
	 *            Vector with values of the fourth column
	 */
	public Matrixd(DVector col1, DVector col2, DVector col3, DVector col4) {
		m00 = col1.x();
		m10 = col1.y();
		m20 = col1.z();

		m01 = col2.x();
		m11 = col2.y();
		m21 = col2.z();

		m02 = col3.x();
		m12 = col3.y();
		m22 = col3.z();
	}

	/**
	 * Sets this matrix to the identity matrix.
	 */
	public final void setIdentity() {
		m00 = 1d;
		m11 = 1d;
		m22 = 1d;

		m01 = 0d;
		m02 = 0d;
		m10 = 0d;
		m12 = 0d;
		m20 = 0d;
		m21 = 0d;
	}

	/**
	 * Adds this matrix to another matrix.
	 *
	 * @param other
	 *            The other matrix
	 *
	 * @return Sum of this + other
	 */
	public Matrixd add(Matrixd other) {
		Matrixd result = new Matrixd();

		result.m00 = this.m00 + other.m00;
		result.m10 = this.m10 + other.m10;
		result.m20 = this.m20 + other.m20;

		result.m01 = this.m01 + other.m01;
		result.m11 = this.m11 + other.m11;
		result.m21 = this.m21 + other.m21;

		result.m02 = this.m02 + other.m02;
		result.m12 = this.m12 + other.m12;
		result.m22 = this.m22 + other.m22;

		return result;
	}

	/**
	 * Negates this matrix.
	 *
	 * @return Negated matrix
	 */
	public Matrixd negate() {
		return multiply(-1f);
	}

	/**
	 * Subtracts this matrix from another matrix.
	 *
	 * @param other
	 *            The other matrix
	 *
	 * @return Difference of this - other
	 */
	public Matrixd subtract(Matrixd other) {
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
	public Matrixd multiply(double scalar) {
		Matrixd result = new Matrixd();

		result.m00 = this.m00 * scalar;
		result.m10 = this.m10 * scalar;
		result.m20 = this.m20 * scalar;

		result.m01 = this.m01 * scalar;
		result.m11 = this.m11 * scalar;
		result.m21 = this.m21 * scalar;

		result.m02 = this.m02 * scalar;
		result.m12 = this.m12 * scalar;
		result.m22 = this.m22 * scalar;

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
	public DVector multiply(DVector vector) {
		double x = this.m00 * vector.x() + this.m01 * vector.y() + this.m02 * vector.z();
		double y = this.m10 * vector.x() + this.m11 * vector.y() + this.m12 * vector.z();
		double z = this.m20 * vector.x() + this.m21 * vector.y() + this.m22 * vector.z();
		return new DVector(x, y, z);
	}

	/**
	 * Multiplies this matrix to another matrix.
	 *
	 * @param other
	 *            The other matrix
	 *
	 * @return Matrix product of this * other
	 */
	public Matrixd multiply(Matrixd other) {
		Matrixd result = new Matrixd();

		result.m00 = this.m00 * other.m00 + this.m01 * other.m10 + this.m02 * other.m20;
		result.m10 = this.m10 * other.m00 + this.m11 * other.m10 + this.m12 * other.m20;
		result.m20 = this.m20 * other.m00 + this.m21 * other.m10 + this.m22 * other.m20;

		result.m01 = this.m00 * other.m01 + this.m01 * other.m11 + this.m02 * other.m21;
		result.m11 = this.m10 * other.m01 + this.m11 * other.m11 + this.m12 * other.m21;
		result.m21 = this.m20 * other.m01 + this.m21 * other.m11 + this.m22 * other.m21;

		result.m02 = this.m00 * other.m02 + this.m01 * other.m12 + this.m02 * other.m22;
		result.m12 = this.m10 * other.m02 + this.m11 * other.m12 + this.m12 * other.m22;
		result.m22 = this.m20 * other.m02 + this.m21 * other.m12 + this.m22 * other.m22;

		return result;
	}

	/**
	 * Transposes this matrix.
	 *
	 * @return Transposed matrix
	 */
	public Matrixd transpose() {
		Matrixd result = new Matrixd();

		result.m00 = this.m00;
		result.m10 = this.m01;
		result.m20 = this.m02;

		result.m01 = this.m10;
		result.m11 = this.m11;
		result.m21 = this.m12;

		result.m02 = this.m20;
		result.m12 = this.m21;
		result.m22 = this.m22;

		return result;
	}

	/**
	 * Stores the matrix in a given Buffer.
	 *
	 * @param buffer
	 *            The buffer to store the matrix data
	 */
	public void toBuffer(DoubleBuffer buffer) {
		buffer.put(m00).put(m10).put(m20);
		buffer.put(m01).put(m11).put(m21);
		buffer.put(m02).put(m12).put(m22);
		buffer.flip();
	}

	/**
	 * Rotation about the y-axis phi radians.
	 * 
	 * @param phi
	 *            angle to rotate about y-axis
	 * @return a rotation operator about y
	 */
	public static Matrixd yRotate(double angle) {
		Matrixd rotate = new Matrixd();

		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		rotate.m00 = cos;
		rotate.m02 = sin;
		rotate.m20 = -sin;
		rotate.m22 = cos;

		return rotate;
	}

	/**
	 * Rotation about the x-axis theta radians.
	 * 
	 * @param theta
	 *            angle to rotate about x-axis
	 * @return a rotation operator about x
	 */
	public static Matrixd xRotate(double angle) {
		Matrixd rotate = new Matrixd();

		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		rotate.m11 = cos;
		rotate.m12 = -sin;
		rotate.m21 = sin;
		rotate.m22 = cos;

		return rotate;
	}

	/**
	 * Rotation about the z-axis theta radians.
	 * 
	 * @param theta
	 *            angle to rotate about z-axis
	 * @return a rotation operator about z
	 */
	public static Matrixd zRotate(double angle) {
		Matrixd rotate = new Matrixd();

		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		rotate.m00 = cos;
		rotate.m01 = -sin;
		rotate.m10 = sin;
		rotate.m11 = cos;

		return rotate;
	}

	/**
	 * Creates a rotation matrix. Similar to
	 * <code>glRotate(angle, x, y, z)</code>.
	 *
	 * @param angle
	 *            Angle of rotation in degrees
	 * @param x
	 *            x coordinate of the rotation vector
	 * @param y
	 *            y coordinate of the rotation vector
	 * @param z
	 *            z coordinate of the rotation vector
	 *
	 * @return Rotation matrix
	 */
	public static Matrixd rotate(double angle, double x, double y, double z) {
		Matrixd rotation = new Matrixd();

		double c = Math.cos(Math.toRadians(angle));
		double s = Math.sin(Math.toRadians(angle));
		DVector vec = new DVector(x, y, z);
		if (vec.mag() != 1f) {
			vec.normalize();
			x = vec.x();
			y = vec.y();
			z = vec.z();
		}

		rotation.m00 = x * x * (1f - c) + c;
		rotation.m10 = y * x * (1f - c) + z * s;
		rotation.m20 = x * z * (1f - c) - y * s;
		rotation.m01 = x * y * (1f - c) - z * s;
		rotation.m11 = y * y * (1f - c) + c;
		rotation.m21 = y * z * (1f - c) + x * s;
		rotation.m02 = x * z * (1f - c) + y * s;
		rotation.m12 = y * z * (1f - c) - x * s;
		rotation.m22 = z * z * (1f - c) + c;

		return rotation;
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
	public static Matrixd scale(double x, double y, double z) {
		Matrixd scaling = new Matrixd();

		scaling.m00 = x;
		scaling.m11 = y;
		scaling.m22 = z;

		return scaling;
	}
	
	public static Matrixd toRotatingFrame(DVector d) {
		return toRotatingFrame(d.x(), d.y(), d.z());
	}

	public static Matrixd toRotatingFrame(double phi, double theta, double psi) {
//		float st = (float) Math.sin(theta);
//		float ct = (float) Math.cos(theta);
//		float sp = (float) Math.sin(phi);
//		float cp = (float) Math.cos(phi);
//
//		return Matrixd.rotate(phi, 0, 0, 1).multiply(Matrixd.rotate(theta, -sp, cp, 0))
//				.multiply(Matrixd.rotate(psi, cp * st, sp * st, ct));
		return Matrixd.zRotate(-psi).multiply(Matrixd.yRotate(-theta)).multiply(Matrixd.zRotate(-phi));
	}
	
	public static Matrixd toSpaceFrame(DVector d) {
		return toSpaceFrame(d.x(), d.y(), d.z());
	}

	public static Matrixd toSpaceFrame(double phi, double theta, double psi) {
//		phi *= -1;
//		theta *= -1;
//		psi *= -1;
//
//		float st = (float) Math.sin(theta);
//		float ct = (float) Math.cos(theta);
//		float sp = (float) Math.sin(phi);
//		float cp = (float) Math.cos(phi);
//
//		return Matrixd.rotate(psi, cp * st, sp * st, ct).multiply(Matrixd.rotate(theta, -sp, cp, 0))
//				.multiply(Matrixd.rotate(phi, 0, 0, 1));
		return Matrixd.zRotate(phi).multiply(Matrixd.yRotate(theta)).multiply(Matrixd.zRotate(psi));
	}
	
	public String toString() {
		return "" + m00 + "\t" + m01 + "\t" + m02 + "\n" + 
				m10 + "\t" + m11 + "\t" + m12 + "\n" + 
				m20 + "\t" + m21 + "\t" + m22 + "\n";
	}
	
	public String roundString() {
		return "" + round(m00) + "\t" + round(m01) + "\t" + round(m02) + "\n" + 
				round(m10) + "\t" + round(m11) + "\t" + round(m12) + "\n" + 
				round(m20) + "\t" + round(m21) + "\t" + round(m22) + "\n";
	}
	
	private static float round(double d) {
		return Math.round(1000 * d) / 1000f;
	}
}
