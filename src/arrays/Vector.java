package arrays;

import java.lang.reflect.Array;

public class Vector<T> {
	
	private T[] elements;
	
	@SuppressWarnings("unchecked")
	public Vector(Class<T> type, int size) {
		elements = (T[]) Array.newInstance(type, size);
	}
	
	public Vector(T[] t) {
		elements = AO.clone(t);
	}
	
	public T get(int index) {
		return elements[index];
	}
	
	public void set(int index, T t) {
		elements[index] = t;
	}
	
}
