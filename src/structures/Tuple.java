package structures;

public class Tuple<E, F> {
	
	private E e;
	private F f;
	
	public Tuple(E e, F f) {
		this.e = e;
		this.f = f;
	}
	
	public E e() {
		return e;
	}
	
	public F f() {
		return f;
	}
	
}
