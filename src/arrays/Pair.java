package arrays;

import numbers.InvalidOperationException;

public class Pair<X, Y> {
	
	private X x;
	private Y y;
	
	public Pair (X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public X x() {
		return x;
	}
	
	public Y y() {
		return y;
	}
	
	public void setX(X x) {
		this.x = x;
	}
	
	public void setY(Y y) {
		this.y = y;
	}
	
	@SuppressWarnings("unchecked")
	public void swap() throws InvalidOperationException {
		if (x.getClass() != y.getClass())
			throw new InvalidOperationException("Cannot perform swap operation on Pair of 2 different types");
		Y a = (Y) x;
		x = (X) y;
		y = a;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair<?, ?>))
			return false;
		return x.equals(((Pair<?, ?>) o).x()) && y.equals(((Pair<?, ?>) o).y());
	}
	
}
