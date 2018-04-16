package tensor;

import java.nio.FloatBuffer;

public class Vector {
	
	protected float x, y, z;
	
	public static final Vector X = new Vector(1, 0, 0);
	public static final Vector Y = new Vector(0, 1, 0);
	public static final Vector Z = new Vector(0, 0, 1);
	
	public static final Vector ZERO = new Vector(0, 0, 0);
	
	public Vector(float x, float y, float z) {
		set(x, y, z);
	}
	
	public Vector(Vector v) {
		this(v.x, v.y, v.z);
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
	
	public void x(float x) {
		this.x = x;
	}
	
	public void y(float y) {
		this.y = y;
	}
	
	public void z(float z) {
		this.z = z;
	}
	
	public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y).put(z);
        buffer.flip();
    }
	
	public DVector toDVector() {
		return new DVector(x, y, z);
	}
	
	protected void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector plus(Vector v) {
		return new Vector(x + v.x, y + v.y, z + v.z);
	}
	
	public void add(Vector v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	
	public Vector minus(Vector v) {
		return new Vector(x - v.x, y - v.y, z - v.z);
	}
	
	public void subtract(Vector v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	
	public Vector times(float s) {
		return new Vector(x * s, y * s, z * s);
	}
	
	public void scale(float s) {
		x *= s;
		y *= s;
		z *= s;
	}
	
	
	public Vector divide(float s) {
		return new Vector(x / s, y / s, z / s);
	}
	
	public void div(float s) {
		x /= s;
		y /= s;
		z /= s;
	}
	
	
	public float dot(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public Vector cross(Vector v) {
		return new Vector(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
	}
	
	
	public float mag2() {
		return x * x + y * y + z * z;
	}
	
	public float mag() {
		return (float) Math.sqrt(mag2());
	}
	
	public void normalize() {
		div(mag());
	}
	
	public Vector normalized() {
		return new Vector(this.divide(mag()));
	}
	
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
}
