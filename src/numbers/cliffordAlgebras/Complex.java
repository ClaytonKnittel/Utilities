package numbers.cliffordAlgebras;

import numbers.InvalidOperationException;

public class Complex implements ComplexNumber {
	

	private float a, b;
	
	public Complex(float a, float b) {
		this.a = a;
		this.b = b;
	}
	
	public Complex(Complex c) {
		this.a = c.a;
		this.b = c.b;
	}

	public float a() {
		return this.a;
	}
	
	public float b() {
		return this.b;
	}
	
	@Override
	public void add(ComplexNumber c) {
		if (!(c instanceof Complex))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		this.a += ((Complex) c).a;
		this.b += ((Complex) c).b;
	}

	@Override
	public void sub(ComplexNumber c) {
		if (!(c instanceof Complex))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		this.a -= ((Complex) c).a;
		this.b -= ((Complex) c).b;
	}

	@Override
	public void mult(ComplexNumber c) {
		if (!(c instanceof Complex))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		float a = this.a * ((Complex) c).a - this.b * ((Complex) c).b;
		this.b = this.a * ((Complex) c).b + this.b * ((Complex) c).a;
		this.a = a;
	}

	@Override
	public void div(Number n) {
		this.a /= n.intValue();
		this.b /= n.intValue();
	}
	
	@Override
	public ComplexNumber plus(ComplexNumber c) {
		if (!(c instanceof Complex))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		ComplexNumber ret = new Complex(this);
		ret.add(c);
		return ret;
	}

	@Override
	public ComplexNumber minus(ComplexNumber c) {
		if (!(c instanceof Complex))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		ComplexNumber ret = new Complex(this);
		ret.sub(c);
		return ret;
	}

	@Override
	public ComplexNumber times(ComplexNumber c) {
		if (!(c instanceof Complex))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		ComplexNumber ret = new Complex(this);
		ret.mult(c);
		return ret;
	}

	@Override
	public ComplexNumber divide(Number n) {
		ComplexNumber ret = new Complex(this);
		ret.div(n);
		return ret;
	}

	@Override
	public ComplexNumber c() {
		ComplexNumber ret = new Complex(this);
		ret.conjugate();
		return ret;
	}
	
	@Override
	public void conjugate() {
		this.b = -this.b;
	}
	
	public float mag() {
		return (float) Math.sqrt(absSquare());
	}
	
	public float absSquare() {
		return ((Complex) this.times(this.c())).a;
	}
	
	public String toString() {
		return a + " " + (b < 0 ? "-" : "+") + " " + Math.abs(b) + "i";
	}
	

}
