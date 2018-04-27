package tensor;

import java.nio.DoubleBuffer;

public class DVector2 {
	
	private double x, y;
	
	public static final DVector2 X = new DVector2(1, 0);
	public static final DVector2 Y = new DVector2(0, 1);
	
	public static final DVector2 ZERO = new DVector2(0, 0);
	
	public DVector2(double x, double y) {
		set(x, y);
	}
	
	public DVector2(DVector2 v) {
		this(v.x, v.y);
	}
	
	public DVector2(DVector v) {
		this(v.x(), v.y());
	}
	
	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	public void toBuffer(DoubleBuffer buffer) {
        buffer.put(x).put(y);
        buffer.flip();
    }
	
	public Vector2 toVector2() {
		return new Vector2((float) x, (float) y);
	}
	
	protected void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public DVector2 plus(DVector2 v) {
		return new DVector2(x + v.x, y + v.y);
	}
	
	public void add(DVector2 v) {
		x += v.x;
		y += v.y;
	}
	
	
	public DVector2 minus(DVector2 v) {
		return new DVector2(x - v.x, y - v.y);
	}
	
	public void subtract(DVector2 v) {
		x -= v.x;
		y -= v.y;
	}
	
	
	public DVector2 times(double s) {
		return new DVector2(x * s, y * s);
	}
	
	public void scale(double s) {
		x *= s;
		y *= s;
	}
	
	
	public DVector2 divide(double s) {
		return new DVector2(x / s, y / s);
	}
	
	public void div(double s) {
		x /= s;
		y /= s;
	}
	
	
	public double dot(DVector2 v) {
		return x * v.x + y * v.y;
	}
	
	
	public double mag2() {
		return x * x + y * y;
	}
	
	public double mag() {
		return Math.sqrt(mag2());
	}
	
	public void normalize() {
		div(mag());
	}
	
	public DVector2 normalized() {
		return new DVector2(this.divide(mag()));
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}
