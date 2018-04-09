package numbers.cliffordAlgebras;

import tensor.DVector;
import tensor.Vector;

/**
 * 
 * @author claytonknittel
 * 
 * Quaternions of the form a + bi + cj + dk
 *
 */
public class Quaternion extends Vector {
	
	private float a;
	
	public static Quaternion X = new Quaternion(Vector.X);
	public static Quaternion Y = new Quaternion(Vector.Y);
	public static Quaternion Z = new Quaternion(Vector.Z);
	public static Quaternion ZERO = new Quaternion(Vector.ZERO);
	
	public Quaternion(float a, float b, float c, float d) {
		super(b, c, d);
		this.a = a;
	}
	
	public Quaternion(float a, Vector v) {
		this(a, v.x(), v.y(), v.z());
	}
	
	public Quaternion(Vector v) {
		this(0, v);
	}
	
	public Quaternion(double a, DVector v) {
		this((float) a, v.toVector());
	}
	
	public Quaternion(Quaternion q) {
		this(q.a, q.b(), q.c(), q.d());
	}
	
	public float a() {
		return a;
	}
	
	public float b() {
		return x();
	}
	
	public float c() {
		return y();
	}
	
	public float d() {
		return z();
	}
	
	private void set(float a, float b, float c, float d) {
		this.a = a;
		super.set(b, c, d);
	}
	
	
	public void add(Quaternion q) {
		a += q.a;
		super.add(q);
	}
	
	public Quaternion plus(Quaternion q) {
		return new Quaternion(a + q.a, super.plus(q));
	}
	
	
	public void sub(Quaternion q) {
		a -= q.a;
		super.subtract(q);
	}
	
	public Quaternion minus(Quaternion q) {
		return new Quaternion(a - q.a, super.minus(q));
	}
	
	public Quaternion times(Quaternion q) {
		return new Quaternion(
				a * q.a - super.dot(q),
				a * q.b() + b() * q.a + c() * q.d() - d() * q.c(),
				a * q.c() + c() * q.a - b() * q.d() + d() * q.b(),
				a * q.d() + d() * q.a + b() * q.c() - c() * q.b());
	}
	
	public void multiply(Quaternion q) {
		set(a * q.a - super.dot(q),
			a * q.b() + b() * q.a + c() * q.d() - d() * q.c(),
			a * q.c() + c() * q.a - b() * q.d() + d() * q.b(),
			a * q.d() + d() * q.a + b() * q.c() - c() * q.b());
	}
	
	public Quaternion times(float s) {
		return new Quaternion(a * s, b() * s, c() * s, d() * s);
	}
	
	public void scale(float s) {
		a *= s;
		this.scale(s);
	}
	
	public float re() {
		return a;
	}
	
	public float mag2() {
		return super.dot(this);
	}
	
	public float im() {
		return (float) Math.sqrt(mag2());
	}
	
	public Quaternion conjugate() {
		return new Quaternion(a, -b(), -c(), -d());
	}
	
	public Quaternion rotate(Vector v) {
		return rotate(new Quaternion(v));
	}
	
	public Quaternion rotate(Quaternion q) {
		return this.times(q).times(conjugate());
	}
	
	public static Quaternion euler(Vector direction, float rotAngle) {
		Vector d = direction.normalized().times((float) Math.sin(rotAngle / 2));
		return new Quaternion((float) Math.cos(rotAngle / 2), d.x(), d.y(), d.z());
	}
	
	public String toString() {
		return a + (b() < 0 ? " - " : " + ") + Math.abs(b()) + (c() < 0 ? " - " : " + ") + Math.abs(c()) + (d() < 0 ? " - " : " + ") + Math.abs(d());
	}
	
}
