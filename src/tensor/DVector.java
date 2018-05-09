package tensor;

import static tensor.DMatrixN.round;

public class DVector {

	private double x, y, z;
	
	public static final DVector X = new DVector(1, 0, 0);
	public static final DVector Y = new DVector(0, 1, 0);
	public static final DVector Z = new DVector(0, 0, 1);
	
	public static final DVector ZERO = new DVector(0, 0, 0);
	
	public DVector(double x, double y, double z) {
		set(x, y, z);
	}
	
	public DVector(DVector v) {
		this(v.x, v.y, v.z);
	}
	
	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	public double z() {
		return z;
	}
	
	public Vector toVector() {
		return new Vector((float) x, (float) y, (float) z);
	}
	
	protected void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public DVector plus(DVector v) {
		return new DVector(x + v.x, y + v.y, z + v.z);
	}
	
	public void add(DVector v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	
	public DVector minus(DVector v) {
		return new DVector(x - v.x, y - v.y, z - v.z);
	}
	
	public void subtract(DVector v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	
	public DVector times(double s) {
		return new DVector(x * s, y * s, z * s);
	}
	
	public void scale(double s) {
		x *= s;
		y *= s;
		z *= s;
	}
	
	
	public DVector divide(double s) {
		return new DVector(x / s, y / s, z / s);
	}
	
	public void div(double s) {
		x /= s;
		y /= s;
		z /= s;
	}
	
	
	public DVector prod(DVector v) {
		return new DVector(x * v.x, y * v.y, z * v.z);
	}
	
	public double dot(DVector v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public DVector cross(DVector v) {
		return new DVector(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
	}
	
	
	public double mag2() {
		return x * x + y * y + z * z;
	}
	
	public double mag() {
		return Math.sqrt(mag2());
	}
	
	public void normalize() {
		div(mag());
	}
	
	public DVector normalized() {
		return new DVector(this.divide(mag()));
	}
	
	public String toString() {
		return "(" + round(x) + ", " + round(y) + ", " + round(z) + ")";
	}
	
}
