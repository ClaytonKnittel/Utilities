package tensor;

import java.nio.FloatBuffer;

public class Vector2 {
	
	protected float x, y;
	
	public static final Vector2 X = new Vector2(1, 0);
	public static final Vector2 Y = new Vector2(0, 1);
	
	public static final Vector2 ZERO = new Vector2(0, 0);
	
	public Vector2(float x, float y) {
		set(x, y);
	}
	
	public Vector2(Vector2 v) {
		this(v.x, v.y);
	}
	
	public Vector2(Vector v) {
		this(v.x, v.y);
	}
	
	public float x() {
		return x;
	}
	
	public float y() {
		return y;
	}
	
	public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y);
        buffer.flip();
    }
	
	public DVector2 toDVector2() {
		return new DVector2(x, y);
	}
	
	protected void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 plus(Vector2 v) {
		return new Vector2(x + v.x, y + v.y);
	}
	
	public void add(Vector2 v) {
		x += v.x;
		y += v.y;
	}
	
	
	public Vector2 minus(Vector2 v) {
		return new Vector2(x - v.x, y - v.y);
	}
	
	public void subtract(Vector2 v) {
		x -= v.x;
		y -= v.y;
	}
	
	
	public Vector2 times(float s) {
		return new Vector2(x * s, y * s);
	}
	
	public void scale(float s) {
		x *= s;
		y *= s;
	}
	
	
	public Vector2 divide(float s) {
		return new Vector2(x / s, y / s);
	}
	
	public void div(float s) {
		x /= s;
		y /= s;
	}
	
	
	public float dot(Vector2 v) {
		return x * v.x + y * v.y;
	}
	
	
	public float mag2() {
		return x * x + y * y;
	}
	
	public float mag() {
		return (float) Math.sqrt(mag2());
	}
	
	public void normalize() {
		div(mag());
	}
	
	public Vector2 normalized() {
		return new Vector2(this.divide(mag()));
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}
