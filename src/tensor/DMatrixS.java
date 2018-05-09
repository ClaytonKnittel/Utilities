package tensor;

import static tensor.DMatrixN.round;

/**
 * A specialized matrix type for symmetric matrices (more space efficient)
 * 
 * @author claytonknittel
 *
 */
public class DMatrixS {
	
	/**
	 * indexed across rows, then down columns, only on upper half
	 * 
	 * ex. 3x3 matrix:
	 * 
	 * 1 2 3
	 *   4 5
	 *     6
	 */
	private double[] a;
	protected int n;
	
	/**
	 * Creates a new DMatrixS of dimension n x n.
	 * <p>
	 * With no arguments, it is by default set to the identity
	 * 
	 * @param n
	 */
	public DMatrixS(int n) {
		this(n, true);
	}
	
	private DMatrixS(int n, boolean setIdentity) {
		a = new double[n * (n + 1) / 2];
		this.n = n;
		setIdentity();
	}
	
	public DMatrixS(DMatrixS s) {
		a = new double[s.a.length];
		this.n = s.n;
		for (int i = 0; i < a.length; i++)
			a[i] = s.a[i];
	}
	
	private void setIdentity() {
		int s = n;
		for (int i = 0; i < a.length; i += s--)
			a[i] = 1;
	}
	
	public static DMatrixS zero(int n) {
		return new DMatrixS(n, false);
	}
	
	/**
	 * Compute an interaction matrix between all pairs of a list of objects. The
	 * interaction must be symmetric, otherwise the resultant matrix will not be
	 * correct.
	 * 
	 * @param entities
	 * @param e the interaction function between any two objects
	 * @return the interaction matrix
	 */
	public static <E> DMatrixS interactionMatrix(E[] entities, SymmetricInteraction<E> e) {
		DMatrixS r = new DMatrixS(entities.length, false);
		for (int i = 0; i < entities.length; i++) {
			for (int j = i; j < entities.length; j++)
				r.set(i, j, e.interact(entities[i], entities[j]));
		}
		return r;
	}
	
	public static interface SymmetricInteraction<E> {
		double interact(E e1, E e2);
	}
	
	public double get(int i, int j) {
		return a[index(i, j)];
	}
	
	public void set(int i, int j, double val) {
		a[index(i, j)] = val;
	}
	
	private int index(int i, int j) {
		if (j < i)
			return index(j, i);
		return rowStartIndex(i) + j - i;
	}
	
	private int rowStartIndex(int row) {
		return rowStartIndex(row, n);
	}
	
	private int rowStartIndex(int row, int n) {
		if (row == 0)
			return 0;
		return n + rowStartIndex(row - 1, n - 1);
	}
	
	public DMatrixN toDMatrixN() {
		DMatrixN r = new DMatrixN(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				r.set(i, j, get(i, j));
		}
		return r;
	}
	
	/**
	 * The cholesky factorization satisfies A = L L^T, where
	 * L is a lower triangular matrix with positive diagonal entries.
	 * 
	 * <p>
	 * 
	 * Note: this matrix must be positive definite. This is not checked 
	 * by this method before computing
	 * 
	 * @return the cholesky factorization of this matrix
	 */
	public DMatrixN choleskyFactorization() {
		DMatrixN L = DMatrixN.zero(n);
		DMatrixS dup = new DMatrixS(this);
		for (int j = 0; j < n; j++) {
			L.set(j, j, Math.sqrt(dup.get(j, j)));
			for (int i = j + 1; i < n; i++) {
				L.set(i, j, dup.get(i, j) / L.get(j, j));
				for (int k = j; k < n; k++)
					dup.set(i, k, dup.get(i, k) - L.get(i, j) * L.get(k, j));
			}
		}
		return L;
	}
	
	/**
	 * @return the upper triangle of this matrix after it has been gaussian-eliminated
	 */
	public DMatrixN gaussian() {
		DMatrixN ret = this.toDMatrixN();
		return ret.gaussian();
	}
	
	/**
	 * Gaussian eliminate this matrix, ignoring all below the diagonals, as these
	 * don't matter when determining if a matrix is positive definite.
	 * 
	 * @return whether or not this matrix is positive definite
	 */
	public boolean positiveDefinite() {
		return this.toDMatrixN().positiveDefinite();
	}
	
	public boolean positiveDiagonals() {
		int s = 0;
		for (int i = n; i > 0; i--) {
			if (a[s] <= 0)
				return false;
			s += i;
		}
		return true;
	}
	
	public String toString() {
		String s = "";
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				s += round(get(i, j)) + (j < n - 1 ? "\t" : "");
			}
			s += i < n - 1 ? "\n" : "";
		}
		return s;
	}
	
}
