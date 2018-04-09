package numbers.cliffordAlgebras;

import numbers.InvalidOperationException;

public class ComplexInt implements ComplexNumber {
	
	private int a, b;
	
	public ComplexInt(int a, int b) {
		this.a = a;
		this.b = b;
	}
	
	public ComplexInt(ComplexInt c) {
		this.a = c.a;
		this.b = c.b;
	}
	
	
	public int a() {
		return this.a;
	}
	
	public int b() {
		return this.b;
	}
	
	
	@Override
	public void add(ComplexNumber c) {
		if (!(c instanceof ComplexInt))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		this.a += ((ComplexInt) c).a;
		this.b += ((ComplexInt) c).b;
	}

	@Override
	public void sub(ComplexNumber c) {
		if (!(c instanceof ComplexInt))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		this.a -= ((ComplexInt) c).a;
		this.b -= ((ComplexInt) c).b;
	}

	@Override
	public void mult(ComplexNumber c) {
		if (!(c instanceof ComplexInt))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		int a = this.a * ((ComplexInt) c).a - this.b * ((ComplexInt) c).b;
		this.b = this.a * ((ComplexInt) c).b + this.b * ((ComplexInt) c).a;
		this.a = a;
	}

	@Override
	public void div(Number n) {
		this.a /= n.intValue();
		this.b /= n.intValue();
	}
	
	@Override
	public ComplexNumber plus(ComplexNumber c) {
		if (!(c instanceof ComplexInt))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		ComplexNumber ret = new ComplexInt(this);
		ret.add(c);
		return ret;
	}

	@Override
	public ComplexNumber minus(ComplexNumber c) {
		if (!(c instanceof ComplexInt))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		ComplexNumber ret = new ComplexInt(this);
		ret.sub(c);
		return ret;
	}

	@Override
	public ComplexNumber times(ComplexNumber c) {
		if (!(c instanceof ComplexInt))
			throw new InvalidOperationException("Cannot add incompatible Complex numbers");
		ComplexNumber ret = new ComplexInt(this);
		ret.mult(c);
		return ret;
	}

	@Override
	public ComplexNumber divide(Number n) {
		ComplexNumber ret = new ComplexInt(this);
		ret.div(n);
		return ret;
	}

	@Override
	public ComplexNumber c() {
		ComplexNumber ret = new ComplexInt(this);
		ret.conjugate();
		return ret;
	}
	
	@Override
	public void conjugate() {
		this.b = -this.b;
	}
	
	public int mag() {
		return ((ComplexInt) this.times(this.c())).a;
	}
	
	public String toString() {
		return a + " " + (b < 0 ? "-" : "+") + " " + Math.abs(b) + "i";
	}
	
	
}
