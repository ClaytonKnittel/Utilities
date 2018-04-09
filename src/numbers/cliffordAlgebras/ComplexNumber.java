package numbers.cliffordAlgebras;

public interface ComplexNumber {
	
	void add(ComplexNumber c);
	void sub(ComplexNumber c);
	void mult(ComplexNumber c);
	void div(Number n);
	
	ComplexNumber plus(ComplexNumber c);
	ComplexNumber minus(ComplexNumber c);
	ComplexNumber times(ComplexNumber c);
	ComplexNumber divide(Number n);
	
	ComplexNumber c();
	
	void conjugate();
	
}
