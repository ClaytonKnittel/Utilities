package structures;

public class Triple<E, F, G> {
	
	private E e;
	private F f;
	private G g;
	
	public Triple(E e, F f, G g) {
		this.e = e;
		this.f = f;
		this.g = g;
	}
	
	public E e() {
		return e;
	}
	
	public F f() {
		return f;
	}
	
	public G g() {
		return g;
	}
	
}