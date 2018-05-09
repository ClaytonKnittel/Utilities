package tensor;

/**
 * A class of square matrices.
 * 
 * @author claytonknittel
 *
 */
public class DMatrixN {
	
	private double[] a;
	private int n;
	
	protected static int acc = 3; // how many digits to round matrices to when printing, setting to 0 means don't round
	
	private static int mul;
	
	static {
		int a = acc;
		mul = 1;
		while (a-- > 0)
			mul *= 10;
	}
	
	/**
	 * Creates a new DMatrixN of dimension n x n.
	 * <p>
	 * With no arguments, it is by default set to the identity
	 * 
	 * @param n
	 */
	public DMatrixN(int n) {
		this(n, true);
	}
	
	private DMatrixN(int n, boolean setIdentity) {
		a = new double[n * n];
		this.n = n;
		if (setIdentity)
			setIdentity();
	}
	
	public DMatrixN(DMatrixN m) {
		a = new double[m.a.length];
		for (int i = 0; i < a.length; i++)
			a[i] = m.a[i];
	}
	
	public static DMatrixN zero(int n) {
		return new DMatrixN(n, false);
	}
	
	private void setIdentity() {
		for (int i = 0; i < a.length; i += n + 1)
			a[i] = 1;
	}
	
	public double get(int i, int j) {
		return a[i * n + j];
	}
	
	public void set(int i, int j, double val) {
		a[i * n + j] = val;
	}
	
	public DMatrixN transpose() {
		DMatrixN r = zero(n);
		for (int i = 0; i < a.length; i++) {
			r.a[(i % n) * n + (i / n)] = a[i];
		}
		return r;
	}
	
	public DMatrixN multiply(DMatrixN m) {
		if (n != m.n)
			throw new IllegalArgumentException("Cannot multiply matrix of dimension " + n + " x " + n + " with matrix of dimension " + m.n + " x " + m.n);
		DMatrixN res = zero(n);
		int t, o; // offsets for this and m, respectively
		for (int i = 0; i < a.length; i++) {
			t = i / n;
			o = i % n;
			for (int j = 0; j < n; j++) {
				res.a[i] += a[t + j] * m.a[o + n * j];
			}
		}
		return res;
	}
	
	public DVectorN multiply(DVectorN v) {
		if (v.dim() != n)
			throw new IllegalArgumentException("Cannot multiply matrix of dimension " + n + " x " + n + " with vector of dimension " + v.dim());
		DVectorN res = new DVectorN(n);
		for (int i = 0; i < a.length; i++) {
			res.add(i / n, a[i] * v.get(i % n));
		}
		return res;
	}
	
	public String toString() {
		String ret = "";
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				ret += round(get(i, j)) + "\t";
			ret += i < n - 1 ? "\n" : "";
		}
		return ret;
	}
	
	protected static double round(double d) {
		if (acc == 0)
			return d;
		return (double) Math.round(d * mul) / mul;
	}
	
}
