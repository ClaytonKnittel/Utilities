package tensor;

import java.nio.FloatBuffer;

import static tensor.DMatrixN.round;

public class Vector4 {
	
	protected float x, y, z, w;
	
	public static final Vector4 X = new Vector4(1, 0, 0, 1);
	public static final Vector4 Y = new Vector4(0, 1, 0, 1);
	public static final Vector4 Z = new Vector4(0, 0, 1, 1);
	
	public static final Vector4 ZERO = new Vector4(0, 0, 0, 0);
	
	public Vector4(float x, float y, float z, float w) {
		set(x, y, z, w);
	}
	
	public Vector4(Vector4 v) {
		this(v.x, v.y, v.z, v.w);
	}
	
	public float x() {
		return x;
	}
	
	public float y() {
		return y;
	}
	
	public float z() {
		return z;
	}
	
	public float w() {
		return w;
	}
	
	public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y).put(z).put(w);
        buffer.flip();
    }
	
	protected void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4 plus(Vector4 v) {
		return new Vector4(x + v.x, y + v.y, z + v.z, w + v.w);
	}
	
	public void add(Vector4 v) {
		x += v.x;
		y += v.y;
		z += v.z;
		w += v.w;
	}
	
	
	public Vector4 minus(Vector4 v) {
		return new Vector4(x - v.x, y - v.y, z - v.z, w - v.w);
	}
	
	public void subtract(Vector4 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		w -= v.w;
	}
	
	
	public Vector4 times(float s) {
		return new Vector4(x * s, y * s, z * s, w * s);
	}
	
	public void scale(float s) {
		x *= s;
		y *= s;
		z *= s;
		w *= s;
	}
	
	
	public Vector4 divide(float s) {
		return new Vector4(x / s, y / s, z / s, w / s);
	}
	
	public void div(float s) {
		x /= s;
		y /= s;
		z /= s;
		w /= s;
	}
	
	
	public float dot(Vector4 v) {
		return x * v.x + y * v.y + z * v.z + w * v.w;
	}
	
	
	public float mag2() {
		return x * x + y * y + z * z + w * w;
	}
	
	public float mag() {
		return (float) Math.sqrt(mag2());
	}
	
	public void normalize() {
		div(mag());
	}
	
	public Vector4 normalized() {
		return new Vector4(this.divide(mag()));
	}
	
	public String toString() {
		return "(" + round(x) + ", " + round(y) + ", " + round(z) + ", " + round(w) + ")";
	}
	
}
