package tensor;

import java.util.List;

import org.jblas.DoubleMatrix;
import org.jblas.Solve;


/**
 * A class of square matrices.
 * 
 * @author claytonknittel
 *
 */
public class DMatrixN {
	
	protected DoubleMatrix m;
	
	protected static int acc = 4; // how many digits to round matrices to when printing, setting to 0 means don't round
	
	private static int mul;
	
	static {
		int a = acc;
		mul = 1;
		while (a-- > 0)
			mul *= 10;
	}
	
	@SuppressWarnings("unused")
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
		if (setIdentity)
			m = DoubleMatrix.eye(n);
		else
			m = new DoubleMatrix(n, n);
	}
	
	public DMatrixN(DMatrixN m) {
		this.m = new DoubleMatrix(m.m.rows, m.m.columns);
		for (int i = 0; i < this.m.length; i++)
			this.m.data[i] = m.m.data[i];
	}
	
	public DMatrixN(int rows, int cols, double...args) {
		m = new DoubleMatrix(rows, cols, args);
	}
	
	protected DMatrixN(DoubleMatrix d) {
		this.m = d;
	}
	
	public static DMatrixN rand(int w) {
		return new DMatrixN(DoubleMatrix.rand(w, w));
	}
	
	public static DMatrixN rand(int rows, int cols) {
		return new DMatrixN(DoubleMatrix.rand(rows, cols));
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
	
	public double get(int i, int j) {
		return m.data[i * m.columns + j];
	}
	
	public void set(int i, int j, double val) {
		m.data[i * m.columns + j] = val;
	}
	
	public void add(int i, int j, double val) {
		m.data[i * m.columns + j] += val;
	}
	
	public DVectorN getRow(int row) {
		DVectorN ret = new DVectorN(m.columns);
		for (int i = 0; i < m.columns; i++)
			ret.set(i, get(row, i));
		return ret;
	}
	
	public DVectorN getRowPart(int row, List<Integer> elsToInclude) {
		DVectorN ret = new DVectorN(elsToInclude.size());
		int i = 0;
		for (Integer l : elsToInclude)
			ret.set(i++, get(row, l));
		return ret;
	}
	
	public DVectorN getCol(int col) {
		DVectorN ret = new DVectorN(m.rows);
		for (int i = 0; i < m.rows; i++)
			ret.set(i, get(i, col));
		return ret;
	}
	
	public DVectorN getColPart(int col, List<Integer> elsToInclude) {
		DVectorN ret = new DVectorN(elsToInclude.size());
		int i = 0;
		for (Integer l : elsToInclude)
			ret.set(i++, get(l, col));
		return ret;
	}
	
	public DMatrixN transpose() {
		return new DMatrixN(m.transpose());
	}
	
	public DMatrixN multiply(DMatrixN m) {
		return new DMatrixN(this.m.mmul(m.m));
	}
	
	public DVectorN multiply(DVectorN v) {
		if (v.dim() != m.columns)
			throw new IllegalArgumentException("Cannot multiply matrix of dimension " + m.rows + " x " + m.columns + " with vector of dimension " + v.dim());
		return new DVectorN(m.mmul(v.v));
	}
	
	/**
	 * 
	 * @param elsToInclude which indices to include in the partitioned matrix
	 * @return a matrix containing only elements whose row and column numbers are in elsToInclude
	 */
	public DMatrixN partitionedMatrix(List<Integer> elsToInclude) {
		DMatrixN part = new DMatrixN(elsToInclude.size(), false);
		int row = 0, col;
		for (Integer i : elsToInclude) {
			col = 0;
			for (Integer j : elsToInclude) {
				part.set(row, col++, get(i, j));
			}
			row++;
		}
		return part;
	}
	
	/**
	 * Solves an equation of the form a x = b
	 * 
	 * @param a n x n symmetrix matrix
	 * @param b n-dimensional vector
	 * @return x, an n-dimensional vector satisfying a x = b
	 */
	public static DVectorN solve(DMatrixN a, DVectorN b) {
		if (a.m.length == 0)
			return new DVectorN(0);
		return new DVectorN(Solve.solve(a.m, b.v));
	}
	
//	private static DVectorN oldSolve(DMatrixN a, DVectorN b) {
//		Augmented m = new Augmented(new DMatrixN(a), new DVectorN(b));
//		int col = 0;
//		all: for (int i = 0; i < m.a.m.rows - 1; i++) {
//			while (m.a.get(i, col) == 0) {
//				if (col == m.a.m.columns)
//					break all;
//				if (m.swapNonzero(i, col))
//					break;
//				col++;
//			}
//			for (int j = i + 1; j < m.a.m.rows; j++) {
//				m.eliminate(i, j, col);
//			}
//			col++;
//		}
//		for (int i = m.a.m.rows - 1; i > 0; i--) {
//			for (int j = i - 1; j >= 0; j--) {
//				m.eliminate(i, j, i);
//			}
//		}
//		m.normalize();
//		return m.b;
//	}
	
//	private static class Augmented {
//		DMatrixN a;
//		DVectorN b;
//		
//		Augmented(DMatrixN a) {
//			this(a, null);
//		}
//		
//		Augmented(DMatrixN a, DVectorN b) {
//			this.a = a;
//			this.b = b;
//		}
//		
//		/**
//		 * Called as the last step, assuming a is now diagonal
//		 */
//		void normalize() {
//			for (int i = 0; i < b.dim(); i++) {
//				b.set(i, b.get(i) / a.get(i, i));
//			}
//		}
//		
//		void swap(int i, int j) {
//			a.swapRows(i, j);
//			if (b != null) {
//				double c = b.get(i);
//				b.set(i, b.get(j));
//				b.set(j, c);
//			}
//		}
//		
//		/**
//		 * Called on a row/col location that is supposed to be nonzero, but is zero.
//		 * If there is a nonzero element below this, the two rows are swapped an this
//		 * returns true, meaning linear independence could potentially still be preserved,
//		 * otherwise it returns false, meaning there are only zeros below this element.
//		 * 
//		 * @param row
//		 * @param col
//		 * @return whether or not a swap was performed
//		 */
//		boolean swapNonzero(int row, int col) {
//			for (int i = row + 1; i < a.m.columns; i++) {
//				if (a.get(i, col) != 0) {
//					swap(i, row);
//					return true;
//				}
//			}
//			return false;
//		}
//		
//		void eliminate(int row, int to, int col) {
//			add(row, -a.get(to, col) / a.get(row, col), to);
//		}
//		
//		void add(int row, double scale, int to) {
//			for (int i = 0; i < a.m.columns; i++) {
//				a.add(to, i, scale * a.get(row, i));
//			}
//			if (b != null)
//				b.add(to, b.get(row) * scale);
//		}
//		
//		public String toString() {
//			String ret = "";
//			for (int i = 0; i < a.m.rows; i++) {
//				for (int j = 0; j < a.m.columns; j++)
//					ret += round(a.get(i, j)) + "\t";
//				if (b != null)
//					ret += ":  " + b.get(i);
//				ret += "\n";
//			}
//			return ret;
//		}
//	}
	
//	private void swapRows(int i, int j) {
//		double s;
//		for (int a = 0; a < m.columns; a++) {
//			s = get(i, a);
//			set(i, a, get(j, a));
//			set(j, a, s);
//		}
//	}
	
	public String toString() {
		String ret = "";
		for (int i = 0; i < m.rows; i++) {
			for (int j = 0; j < m.columns; j++)
				ret += round(get(i, j)) + "\t";
			ret += i < m.rows - 1 ? "\n" : "";
		}
		return ret;
	}
	
	protected static double round(double d) {
		if (acc == 0)
			return d;
		return (double) Math.round(d * mul) / mul;
	}
	
}
