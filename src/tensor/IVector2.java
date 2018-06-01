package tensor;

import java.nio.IntBuffer;

public class IVector2 {
	
	protected int x, y;
	
	public static final IVector2 X = new IVector2(1, 0);
	public static final IVector2 Y = new IVector2(0, 1);
	
	public static final IVector2 ZERO = new IVector2(0, 0);
	
	public IVector2(int x, int y) {
		set(x, y);
	}
	
	public IVector2(IVector2 v) {
		this(v.x, v.y);
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public Vector2 toVector2() {
		return new Vector2(x, y);
	}
	
	public void toBuffer(IntBuffer buffer) {
        buffer.put(x).put(y);
        buffer.flip();
    }
	
	protected void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public IVector2 plus(int x, int y) {
		return new IVector2(this.x + x, this.y + y);
	}
	
	public IVector2 plus(IVector2 v) {
		return new IVector2(x + v.x, y + v.y);
	}
	
	public void add(IVector2 v) {
		x += v.x;
		y += v.y;
	}
	
	
	public IVector2 minus(int x, int y) {
		return new IVector2(this.x - x, this.y - y);
	}
	
	public IVector2 minus(IVector2 v) {
		return new IVector2(x - v.x, y - v.y);
	}
	
	public void subtract(IVector2 v) {
		x -= v.x;
		y -= v.y;
	}
	
	
	public IVector2 times(int s) {
		return new IVector2(x * s, y * s);
	}
	
	public void scale(int s) {
		x *= s;
		y *= s;
	}
	
	
	public IVector2 divide(int s) {
		return new IVector2(x / s, y / s);
	}
	
	public void div(int s) {
		x /= s;
		y /= s;
	}
	
	
	public int dot(IVector2 v) {
		return x * v.x + y * v.y;
	}
	
	
	public int mag2() {
		return x * x + y * y;
	}
	
	public float mag() {
		return (float) Math.sqrt(mag2());
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof IVector2))
			return false;
		IVector2 r = (IVector2) o;
		return r.x == x && r.y == y;
	}
	
	public int hashCode() {
		int ret = 17;
		ret = 31 * ret + x;
		ret = 31 * ret + y;
		return ret;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}
