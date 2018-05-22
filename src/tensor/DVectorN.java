package tensor;

import static tensor.DMatrixN.round;

import org.jblas.DoubleMatrix;

import methods.P;

public class DVectorN {
	
	protected DoubleMatrix v;
	
	/**
	 * Creates a new DVectorN of dimension n
	 * 
	 * @param n
	 */
	public DVectorN(int n) {
		v = new DoubleMatrix(n);
	}
	
	public DVectorN(double...args) {
		v = new DoubleMatrix(args.length, 1, args);
	}
	
	public DVectorN(DVectorN v) {
		this.v = new DoubleMatrix(v.dim());
		for (int i = 0; i < this.v.length; i++)
			this.v.data[i] = v.v.data[i];
	}
	
	protected DVectorN(DoubleMatrix v) {
		this.v = v;
	}
	
	public static DVectorN rand(int len) {
		return new DVectorN(DoubleMatrix.rand(len));
	}
	
	public static <T> DVectorN functionalMap(T[] elements, Map<T> map) {
		DVectorN r = new DVectorN(elements.length);
		for (int i = 0; i < elements.length; i++)
			r.set(i, map.val(elements[i]));
		return r;
	}
	
	public static interface Map<T> {
		double val(T t);
	}
	
	public double get(int i) {
		return v.data[i];
	}
	
	public void set(int i, double val) {
		v.data[i] = val;
	}
	
	public void add(int i, double val) {
		v.data[i] += val;
	}
	
	public int dim() {
		return v.length;
	}
	
	public void add(DVectorN v) {
		verifySize(v, "addition");
		this.v = this.v.add(v.v);
	}
	
	public DVectorN plus(DVectorN v) {
		verifySize(v, "addition");
		return new DVectorN(this.v.add(v.v));
	}
	
	public void sub(DVectorN v) {
		verifySize(v, "subtraction");
		this.v = this.v.sub(v.v);
	}
	
	public DVectorN minus(DVectorN v) {
		verifySize(v, "subtraction");
		return new DVectorN(this.v.sub(v.v));
	}
	
	public void scale(double s) {
		this.v = this.v.mul(s);
	}
	
	public DVectorN times(double s) {
		return new DVectorN(this.v.mul(s));
	}
	
	public double dot(DVectorN v) {
		verifySize(v, "dot");
		return this.v.dot(v.v);
	}
	
	public DVectorN prod(DVectorN v) {
		verifySize(v, "hadamard product");
		return new DVectorN(this.v.mul(v.v));
	}
	
	private void verifySize(DVectorN other, String operation) {
		if (other.dim() != v.length)
			throw new IllegalArgumentException("Cannot perform " + operation + " operation on vectors of inequal dimensions: " + v.length + " and " + other.dim());
	}
	
	public String toString() {
		if (v.length == 0)
			return "[]";
		String ret = "[";
		for (int i = 0; i < v.length - 1; i++) {
			ret += round(v.data[i]) + ", ";
		}
		return ret + round(v.data[v.length - 1]) + "]";
	}
	
}
