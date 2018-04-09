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
public class DQuaternion extends DVector {
	
	private double a;
	
	public static DQuaternion X = new DQuaternion(DVector.X);
	public static DQuaternion Y = new DQuaternion(DVector.Y);
	public static DQuaternion Z = new DQuaternion(DVector.Z);
	public static DQuaternion ZERO = new DQuaternion(DVector.ZERO);
	
	public DQuaternion(double a, double b, double c, double d) {
		super(b, c, d);
		this.a = a;
	}
	
	public DQuaternion(double a, DVector v) {
		this(a, v.x(), v.y(), v.z());
	}
	
	public DQuaternion(Vector v) {
		this(0, v.x(), v.y(), v.z());
	}
	
	public DQuaternion(DVector v) {
		this(0, v);
	}
	
	public DQuaternion(DQuaternion q) {
		this(q.a(), q.b(), q.c(), q.d());
	}
	
	public double a() {
		return a;
	}
	
	public double b() {
		return x();
	}
	
	public double c() {
		return y();
	}
	
	public double d() {
		return z();
	}
	
	public Quaternion toQuaternion() {
		return new Quaternion(a, this);
	}
	
	private void set(double a, double b, double c, double d) {
		this.a = a;
		super.set(b, c, d);
	}
	
	
	public void add(DQuaternion q) {
		a += q.a;
		super.add(q);
	}
	
	public DQuaternion plus(DQuaternion q) {
		return new DQuaternion(a + q.a, super.plus(q));
	}
	
	
	public void sub(DQuaternion q) {
		a -= q.a;
		super.subtract(q);
	}
	
	public DQuaternion minus(DQuaternion q) {
		return new DQuaternion(a - q.a, super.minus(q));
	}
	
	public DQuaternion times(DQuaternion q) {
		return new DQuaternion(
				a * q.a - super.dot(q),
				a * q.b() + b() * q.a + c() * q.d() - d() * q.c(),
				a * q.c() + c() * q.a - b() * q.d() + d() * q.b(),
				a * q.d() + d() * q.a + b() * q.c() - c() * q.b());
	}
	
	public void multiply(DQuaternion q) {
		set(a * q.a - super.dot(q),
			a * q.b() + b() * q.a + c() * q.d() - d() * q.c(),
			a * q.c() + c() * q.a - b() * q.d() + d() * q.b(),
			a * q.d() + d() * q.a + b() * q.c() - c() * q.b());
	}
	
	public DQuaternion times(double s) {
		return new DQuaternion(a * s, b() * s, c() * s, d() * s);
	}
	
	public void scale(double s) {
		a *= s;
		this.scale(s);
	}
	
	public DQuaternion divide(double s) {
		return new DQuaternion(a / s, b() / s, c() / s, d() / s);
	}
	
	public double re() {
		return a;
	}
	
	public double mag2() {
		return super.dot(this);
	}
	
	public double im() {
		return Math.sqrt(mag2());
	}
	
	public DQuaternion conjugate() {
		return new DQuaternion(a, -b(), -c(), -d());
	}
	
	public DQuaternion rotate(DVector v) {
		return rotate(new DQuaternion(v));
	}
	
	public DQuaternion rotate(DQuaternion q) {
		return this.times(q).times(conjugate());
	}
	
	public static DQuaternion euler(DVector direction, double rotAngle) {
		DVector d = direction.normalized().times(Math.sin(rotAngle / 2));
		return new DQuaternion(Math.cos(rotAngle / 2), d.x(), d.y(), d.z());
	}
	
	public String toString() {
		return a + (b() < 0 ? " - " : " + ") + Math.abs(b()) + (c() < 0 ? " - " : " + ") + Math.abs(c()) + (d() < 0 ? " - " : " + ") + Math.abs(d());
	}
	
}
