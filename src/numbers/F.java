package numbers;

public class F {
	
	private static final double sqrt2Pi = Math.sqrt(2 * Math.PI);
	
	public static float sigmoid(float f) {
		return 1 / (1 + (float) Math.exp(-f));
	}
	
	public static double sigmoid(double d) {
		return 1 / (1 + Math.exp(-d));
	}
	
	public static float gaussian(float f) {
		return (float) Math.exp(-square(f) / 2) / (float) sqrt2Pi;
	}
	
	public static float gaussian(float f, float avg, float stdDev) {
		return (float) Math.exp(-square((f - avg) / stdDev) / 2) / ((float) sqrt2Pi * stdDev);
	}
	
	public static double gaussian(double d) {
		return Math.exp(-square(d) / 2) / sqrt2Pi;
	}
	
	public static double gaussian(double d, double avg, double stdDev) {
		return Math.exp(-square((d - avg) / stdDev) / 2) / (sqrt2Pi * stdDev);
	}
	
	public static double randomBell() {
		return Math.tan(Math.PI * (Math.random() - .5));
	}
	
	public static float square(float f) {
		return f * f;
	}
	
	public static double square(double d) {
		return d * d;
	}
}
