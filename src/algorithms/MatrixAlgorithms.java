package algorithms;

import java.util.LinkedList;

import tensor.DMatrixN;
import tensor.DVectorN;

public class MatrixAlgorithms {
	
	
	/**
	 * Solves an equation of the form d = a f + b,
	 * where all are vectors except a, which is a square matrix.
	 * <p>
	 * The constraints are that all elements of d and f, the two unknowns, must
	 * be nonnegative, and their dot product d.f must be zero. This is to
	 * assure that if an element of f is not zero, its corresponding element
	 * in a is zero, and vice versa.
	 * <p>
	 * This is used to find the constraining forces in a physical system. a
	 * is the matrix of dependencies of accelerations on all of the forces in
	 * consideration, b is a precalculated vector stating each of the initial
	 * accelerations, and f is returned.
	 * 
	 * @param a
	 * @param b
	 * @return f, the set of (commonly) forces that satisfy d = a f + b for a > 0, f > 0, a.f = 0
	 */
	public static DVectorN solveConstrainedEqn(DMatrixN a, DVectorN b) {
		try {
			return solveConstrainedEqnHelper(a, b, new LinkedList<>(), new LinkedList<>(), new DVectorN(b.dim()), 0);
		} catch (StackOverflowError e) {
			System.err.println("Tried to solve: \n" + a + "\n = " + b);
		}
		return new DVectorN(b.dim());
	}
	
	/**
	 * We may assume the first n - 1 forces have been solved for and all
	 * conditions are met for <code>1<=i<=n-1</code>. To find the next force,
	 * group the first n - 1 forces into 2 categories, contact (C) (a = 0) and no
	 * contact (NC) (f = 0).
	 * <p>
	 * To solve, first start with f<sub>n</sub> = 0, then, while a<sub>n</sub> < 0 and all of the conditions are
	 * maintained, continually increase f. If one of the conditions is broken, this means
	 * either a C point reached zero force or an NC point reached zero acceleration. To rectify this,
	 * move the C point to the NC set or the NC point to the C set and keep going. If a<sub>n</sub> = 0, then we
	 * have found fn and may continue the algorithm.
	 * <p>
	 * To put this more mathematically, to find the greatest amount s by which we may increase f<sub>n</sub>,
	 * we can look at how f<sub>n</sub> affects all other values:
	 * <ul>
	 * 	<li> C: for a point in C, we require that A<sub>C</sub> (the part of A that only contains the
	 * elements corresponding to points in C) * &Delta;f = 0, as all contact points with a force must not
	 * move (the normal force can only cancel other forces). If we know how f<sub>i</sub> changes relative to
	 * f<sub>n</sub>, i.e. we know &Delta;f<sub>i</sub>=c<sub>i</sub>&Delta;f<sub>n</sub> for some c<sub>i</sub>, then it is
	 * simply: s = -f<sub>i</sub> / &Delta;f<sub>i</sub> = -f<sub>i</sub> / c<sub>i</sub>
	 * 	<li> NC: for a point in NC, we require that d + a &Delta;f >= 0 for all elements, so the maximum step size would
	 * be s = <sup>-a<sub>i</sub></sup> / <sub>&Sigma;<sub>j &isin; C</sub> a<sub>ij</sub>c<sub>j</sub> + 1</sub> (as all c<sub>j</sub> for j &isin; NC
	 * = 0, and c<sub>n</sub> = 1), where all c<sub>j</sub> have already been calculated (how to do so is discussed below).
	 * 	<li> for f<sub>n</sub> itself, the maximum we can change is constrained by s = <sup>-a<sub>n</sub></sup> /
	 * <sub>&Sigma;<sub>j &isin; C</sub> a<sub>nj</sub>c<sub>j</sub> + 1</sub>
	 * </ul>
	 * <p>
	 * To compute all c<sub>i</sub>, we partition a into A<sub>C</sub>, being the A matrix only consisting of elements corresponding
	 * to points in C, and solve the equation A<sub>C</sub>&Delta;f + a<sub>n</sub>f<sub>n</sub> = 0, where a<sub>n</sub> is the n<sup>th</sup>
	 * row of A. Equivalently, as &Delta;f = c &Delta;f, we solve A<sub>C</sub> c = -a<sub>n</sub> to find all c<sub>i</sub>.
	 * 
	 * @param a
	 * @param b
	 * @param c list of the indices of contact points (a = 0)
	 * @param nc list of the indices of no contact points (f = 0)
	 * @param f the list of forces we are solving for
	 * @param i which force we are currently solving (starts with i = 0)
	 * @return the solution
	 */
	private static DVectorN solveConstrainedEqnHelper(DMatrixN a, DVectorN b, LinkedList<Integer> c, LinkedList<Integer> nc, DVectorN f, int i) {
		if (i == b.dim()) {
			return f;
		}
		
		DVectorN d = a.multiply(f).plus(b);
		DMatrixN ac = a.partitionedMatrix(c);
		DVectorN aCol = a.getColPart(i, c);
		DVectorN aRow = a.getRowPart(i, c);
		DVectorN cv = DMatrixN.solve(ac, aCol.times(-1)); // solves for the changes ci given delta fn = 1
		
		double s = -d.get(i) / (aRow.dot(cv) + a.get(i, i));
		if (s <= 0) {
			nc.add(i);
			return solveConstrainedEqnHelper(a, b, c, nc, f, i + 1);
		}
		
		
		int loc = i;
		double calc;
		boolean inC = false;
		
		int p = 0;
		for (Integer e : c) {
			double num = cv.get(p++);
			if (num >= 0)
				continue;
			
			calc = -f.get(e) / num;
			if (calc < s && calc >= 0) {
				s = calc;
				loc = e;
				inC = true;
			}
		}
		for (Integer e : nc) {
			double num = a.getRowPart(e, c).dot(cv) + 1;
			if (num >= 0)
				continue;
			
			calc = -d.get(e) / num;
			if (calc < s && calc >= 0) {
				s = calc;
				loc = e;
				inC = false;
			}
		}
		
		
		cv.scale(s);
		int el = 0;
		for (Integer e : c)
			f.add(e, cv.get(el++));
		f.add(i, s);
		
		if (loc == i) {
			c.add(i);
			i++;
		}
		else {
			if (inC) {
				c.remove((Integer) loc);
				nc.add(loc);
			} else {
				nc.remove((Integer) loc);
				c.add(loc);
			}
		}
		
		if (s == 0.0) {
			System.out.println("FE " + a + b + f);
		}
		
		return solveConstrainedEqnHelper(a, b, c, nc, f, i);
	}
	
}
