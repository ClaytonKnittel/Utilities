package graphics;

public class Color {
	
	public static final Color red = new Color(0xffff0000);
	public static final Color green = new Color(0xff00ff00);
	public static final Color blue = new Color(0xff0000ff);
	public static final Color yellow = new Color(0xffffff00);
	public static final Color orange = new Color(0xffffa500);
	public static final Color purple = new Color(0xffff00ff);
	public static final Color black = new Color(0xff000000);
	public static final Color white = new Color(0xffffffff);
	public static final Color gray = new Color(0xff808080);
	public static final Color cyan = new Color(0xff00ffff);
	
	public static final Color matte_blue = new Color(0xff0099ff);
	
	private int val;
	
	public Color(int val) {
		this.val = val;
	}
	
	public Color(int r, int g, int b, int alpha) {
		this(alpha << 24 | r << 16 | g << 8 | b);
	}
	
	public Color(int r, int g, int b) {
		this(r, g, b, 0xff);
	}
	
	public int red() {
		return (val >> 16) & 0xff;
	}
	
	public int green() {
		return (val >> 8) & 0xff;
	}
	
	public int blue() {
		return val & 0xff;
	}
	
	public int alpha() {
		return val >>> 24;
	}
	
	public float redf() {
		return red() / 255f;
	}
	
	public float greenf() {
		return green() / 255f;
	}
	
	public float bluef() {
		return blue() / 255f;
	}
	
	public float alphaf() {
		return alpha() / 255f;
	}
	
	public int val() {
		return val;
	}
	
}
