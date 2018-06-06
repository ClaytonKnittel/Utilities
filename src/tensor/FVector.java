package tensor;

import static tensor.DMatrixN.round;

import org.jblas.FloatMatrix;

public class FVector {
	
	protected FloatMatrix v;
	
	public FVector(int size) {
		v = new FloatMatrix(size);
	}
	
	public FVector(float... args) {
		v = new FloatMatrix(args);
	}
	
	public FVector(FVector v) {
		this(v.v);
	}
	
	protected FVector(FloatMatrix v) {
		this.v = v;
	}
	
	public float get(int i) {
		return v.get(i);
	}
	
	public int size() {
		return v.length;
	}
	
	public FVector appendRow(float val) {
		return new FVector(FloatMatrix.concatVertically(new FloatMatrix(1, 1, val), v));
	}
	
	public FVector removeTopRow() {
		float[] n = new float[v.length - 1];
		for (int i = 0; i < n.length; i++)
			n[i] = v.get(i + 1);
		return new FVector(n);
	}
	
	public FVector plus(FVector v) {
		return new FVector(this.v.add(v.v));
	}
	
	public void add(FVector v) {
		this.v = this.v.add(v.v);
	}
	
	
	public FVector minus(FVector v) {
		return new FVector(this.v.sub(v.v));
	}
	
	public void subtract(FVector v) {
		this.v = this.v.sub(v.v);
	}
	
	
	public FVector times(float s) {
		return new FVector(v.mul(s));
	}
	
	public void scale(float s) {
		v = v.mul(s);
	}
	
	
	public FVector divide(float s) {
		return new FVector(v.div(s));
	}
	
	public void div(float s) {
		v = v.div(s);
	}
	
	public float dot(FVector v) {
		return this.v.dot(v.v);
	}
	
	public FMatrix outer(FVector other) {
		return new FMatrix(v.mmul(other.v.transpose()));
	}
	
	public FVector mul(FVector other) {
		return new FVector(v.mul(other.v));
	}
	
	
	public float mag2() {
		return v.dot(v);
	}
	
	public float mag() {
		return (float) Math.sqrt(mag2());
	}
	
	public void normalize() {
		div(mag());
	}
	
	public FVector normalized() {
		return new FVector(v.div(mag()));
	}
	
	public String toString() {
		if (size() == 0)
			return "()";
		String ret = "(";
		for (int i = 0; i < size() - 1; i++)
			ret += round(get(i)) + ", ";
		return ret + round(get(size() - 1)) + ")";
	}
}
