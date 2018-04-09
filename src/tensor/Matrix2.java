package tensor;

import java.nio.FloatBuffer;

/**
 * This class represents a 4x4-Matrix. GLSL equivalent to mat4.
 *
 * @author Heiko Brumme
 */
public class Matrix2 {

    private float m00, m01;
    private float m10, m11;

    /**
     * Creates a 4x4 identity matrix.
     */
    public Matrix2() {
        setIdentity();
    }

    /**
     * Creates a 4x4 matrix with specified columns.
     *
     * @param col1 Vector with values of the first column
     * @param col2 Vector with values of the second column
     * @param col3 Vector with values of the third column
     * @param col4 Vector with values of the fourth column
     */
    public Matrix2(Vector2 col1, Vector2 col2) {
        m00 = col1.x;
        m10 = col1.y;

        m01 = col2.x;
        m11 = col2.y;
    }

    /**
     * Sets this matrix to the identity matrix.
     */
    public final void setIdentity() {
        m00 = 1f;
        m11 = 1f;

        m01 = 0f;
        m10 = 0f;
    }

    /**
     * Adds this matrix to another matrix.
     *
     * @param other The other matrix
     *
     * @return Sum of this + other
     */
    public Matrix2 add(Matrix2 other) {
        Matrix2 result = new Matrix2();

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
    public Matrix2 negate() {
        return multiply(-1f);
    }

    /**
     * Subtracts this matrix from another matrix.
     *
     * @param other The other matrix
     *
     * @return Difference of this - other
     */
    public Matrix2 subtract(Matrix2 other) {
        return this.add(other.negate());
    }

    /**
     * Multiplies this matrix with a scalar.
     *
     * @param scalar The scalar
     *
     * @return Scalar product of this * scalar
     */
    public Matrix2 multiply(float scalar) {
        Matrix2 result = new Matrix2();

        result.m00 = this.m00 * scalar;
        result.m10 = this.m10 * scalar;
        
        result.m01 = this.m01 * scalar;
        result.m11 = this.m11 * scalar;

        return result;
    }

    /**
     * Multiplies this matrix to a vector.
     *
     * @param vector The vector
     *
     * @return Vector product of this * other
     */
    public Vector2 multiply(Vector4 vector) {
        float x = this.m00 * vector.x + this.m01 * vector.y;
        float y = this.m10 * vector.x + this.m11 * vector.y;
        return new Vector2(x, y);
    }

    /**
     * Multiplies this matrix to another matrix.
     *
     * @param other The other matrix
     *
     * @return Matrix product of this * other
     */
    public Matrix2 multiply(Matrix2 other) {
        Matrix2 result = new Matrix2();

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
    public Matrix2 transpose() {
        Matrix2 result = new Matrix2();

        result.m00 = this.m00;
        result.m10 = this.m01;

        result.m01 = this.m10;
        result.m11 = this.m11;

        return result;
    }

    /**
     * Stores the matrix in a given Buffer.
     *
     * @param buffer The buffer to store the matrix data
     */
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(m00).put(m10);
        buffer.put(m01).put(m11);
        buffer.flip();
    }


    /**
     * Creates a scaling matrix. Similar to <code>glScale(x, y, z)</code>.
     *
     * @param x Scale factor along the x coordinate
     * @param y Scale factor along the y coordinate
     * @param z Scale factor along the z coordinate
     *
     * @return Scaling matrix
     */
    public static Matrix2 scale(float x, float y) {
        Matrix2 scaling = new Matrix2();

        scaling.m00 = x;
        scaling.m11 = y;

        return scaling;
    }

}