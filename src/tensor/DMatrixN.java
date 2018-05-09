package tensor;

/**
 * A class of square matrices.
 * 
 * @author claytonknittel
 *
 */
public class DMatrixN {
	
	private double[] a;
	protected int n;
	
	protected static int acc = 3; // how many digits to round matrices to when printing, setting to 0 means don't round
	
	private static int mul;
	
	static {
		int a = acc;
		mul = 1;
		while (a-- > 0)
			mul *= 10;
	}
	
	private DMatrixN() {}
	
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
		n = m.n;
		for (int i = 0; i < a.length; i++)
			a[i] = m.a[i];
	}
	
	public DMatrixN(double...args) {
		double d = Math.round(Math.sqrt(args.length));
		if ((int) d != d)
			throw new IllegalArgumentException("DMatrixN matrices must be square");
		a = args;
		n = (int) d;
	}
	
	public static DMatrixN zero(int n) {
		return new DMatrixN(n, false);
	}
	
	/**
	 * Compute an interaction matrix between all pairs of a list of objects.
	 * 
	 * @param entities
	 * @param e the interaction function between any two objects
	 * @return the interaction matrix
	 */
	public static <E> DMatrixN interactionMatrix(E[] entities, Interaction<E> e) {
		DMatrixN r = new DMatrixN(entities.length, false);
		for (int i = 0; i < entities.length; i++) {
			for (int j = 0; j < entities.length; j++)
				r.set(i, j, e.interact(entities[i], entities[j]));
		}
		return r;
	}
	
	public static interface Interaction<E> {
		double interact(E e1, E e2);
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
	
	public void add(int i, int j, double val) {
		a[i * n + j] += val;
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
			t = i / n * n;
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
	
	/**
	 * Solves an equation of the form a x = b
	 * 
	 * @param a n x n symmetrix matrix
	 * @param b n-dimensional vector
	 * @return x, an n-dimensional vector satisfying a x = b
	 */
	public static DVectorN solve(DMatrixN a, DVectorN b) {
		Augmented m = new Augmented(new DMatrixN(a), new DVectorN(b));
		int col = 0;
		all: for (int i = 0; i < m.a.n - 1; i++) {
			while (m.a.get(i, col) == 0) {
				if (col == m.a.n)
					break all;
				if (m.swapNonzero(i, col))
					break;
				col++;
			}
			for (int j = i + 1; j < m.a.n; j++) {
				m.eliminate(i, j, col);
			}
		}
		for (int i = m.a.n - 1; i > 0; i--) {
			for (int j = i - 1; j >= 0; j--) {
				m.eliminate(i, j, i);
			}
		}
		m.normalize();
		return m.b;
	}
	
	private static class Augmented {
		DMatrixN a;
		DVectorN b;
		
		Augmented(DMatrixN a) {
			this(a, null);
		}
		
		Augmented(DMatrixN a, DVectorN b) {
			this.a = a;
			this.b = b;
		}
		
		/**
		 * Called as the last step, assuming a is now diagonal
		 */
		void normalize() {
			for (int i = 0; i < b.dim(); i++) {
				b.set(i, b.get(i) / a.get(i, i));
			}
		}
		
		void swap(int i, int j) {
			a.swapRows(i, j);
			if (b != null) {
				double c = b.get(i);
				b.set(i, b.get(j));
				b.set(j, c);
			}
		}
		
		/**
		 * Called on a row/col location that is supposed to be nonzero, but is zero.
		 * If there is a nonzero element below this, the two rows are swapped an this
		 * returns true, meaning linear independence could potentially still be preserved,
		 * otherwise it returns false, meaning there are only zeros below this element.
		 * 
		 * @param row
		 * @param col
		 * @return whether or not a swap was performed
		 */
		boolean swapNonzero(int row, int col) {
			for (int i = row + 1; i < a.n; i++) {
				if (a.get(i, col) != 0) {
					swap(i, row);
					return true;
				}
			}
			return false;
		}
		
		void eliminate(int row, int to, int col) {
			add(row, -a.get(to, col) / a.get(row, col), to);
		}
		
		void add(int row, double scale, int to) {
			for (int i = 0; i < a.n; i++) {
				a.add(to, i, scale * a.get(row, i));
			}
			if (b != null)
				b.add(to, b.get(row) * scale);
		}
		
		public String toString() {
			String ret = "";
			for (int i = 0; i < a.n; i++) {
				for (int j = 0; j < a.n; j++)
					ret += round(a.get(i, j)) + "\t";
				if (b != null)
					ret += ":  " + b.get(i);
				ret += "\n";
			}
			return ret;
		}
	}
	
	public DMatrixN gaussian() {
		Augmented m = new Augmented(new DMatrixN(this));
		int col = 0;
		all: for (int i = 0; i < m.a.n - 1; i++) {
			while (m.a.get(i, col) == 0) {
				if (col == m.a.n)
					break all;
				if (m.swapNonzero(i, col))
					break;
				col++;
			}
			for (int j = i + 1; j < m.a.n; j++) {
				m.eliminate(i, j, col);
			}
		}
		return m.a;
	}
	
	private void swapRows(int i, int j) {
		double s;
		for (int a = 0; a < n; a++) {
			s = get(i, a);
			set(i, a, get(j, a));
			set(j, a, s);
		}
	}
	
	public boolean positiveDefinite() {
		return gaussian().positiveDiagonals();
	}
	
	public boolean positiveDiagonals() {
		for (int i = 0; i < a.length; i += n + 1) {
			if (a[i] <= 0)
				return false;
		}
		return true;
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
