package tensor;

import static tensor.DMatrixN.round;

public class DVectorN {
	
	private double[] v;
	
	/**
	 * Creates a new DVectorN of dimension n
	 * 
	 * @param n
	 */
	public DVectorN(int n) {
		v = new double[n];
	}
	
	public DVectorN(double...args) {
		v = args;
	}
	
	public DVectorN(DVectorN v) {
		this.v = new double[v.dim()];
		for (int i = 0; i < this.v.length; i++)
			this.v[i] = v.get(i);
	}
	
	public double get(int i) {
		return v[i];
	}
	
	public void set(int i, double val) {
		v[i] = val;
	}
	
	protected void add(int i, double val) {
		v[i] += val;
	}
	
	public int dim() {
		return v.length;
	}
	
	public void add(DVectorN v) {
		verifySize(v, "addition");
		for (int i = 0; i < this.v.length; i++)
			this.v[i] += v.v[i];
	}
	
	public DVectorN plus(DVectorN v) {
		verifySize(v, "addition");
		DVectorN ret = new DVectorN(this.v.length);
		for (int i = 0; i < this.v.length; i++)
			ret.v[i] = this.v[i] + v.v[i];
		return ret;
	}
	
	public void sub(DVectorN v) {
		verifySize(v, "subtraction");
		for (int i = 0; i < this.v.length; i++)
			this.v[i] -= v.v[i];
	}
	
	public DVectorN minus(DVectorN v) {
		verifySize(v, "subtraction");
		DVectorN ret = new DVectorN(this.v.length);
		for (int i = 0; i < this.v.length; i++)
			ret.v[i] = this.v[i] - v.v[i];
		return ret;
	}
	
	public double dot(DVectorN v) {
		verifySize(v, "dot");
		double ret = 0;
		for (int i = 0; i < this.v.length; i++)
			ret += this.v[i] * v.v[i];
		return ret;
	}
	
	public DVectorN prod(DVectorN v) {
		verifySize(v, "hadamard product");
		DVectorN ret = new DVectorN(this.v.length);
		for (int i = 0; i < this.v.length; i++)
			ret.v[i] = this.v[i] * v.v[i];
		return ret;
	}
	
	private void verifySize(DVectorN other, String operation) {
		if (other.dim() != v.length)
			throw new IllegalArgumentException("Cannot perform " + operation + " operation on vectors of inequal dimensions: " + v.length + " and " + other.dim());
	}
	
	public String toString() {
		String ret = "[";
		for (int i = 0; i < v.length - 1; i++) {
			ret += round(v[i]) + ", ";
		}
		return ret + v[v.length - 1] + "]";
	}
	
}
