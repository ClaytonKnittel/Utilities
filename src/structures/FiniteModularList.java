package structures;

import java.lang.reflect.Array;
import java.util.Iterator;

public class FiniteModularList<E> implements Iterable<E> {
	
	private E[] elements;
	private int loc;
	
	@SuppressWarnings("unchecked")
	public FiniteModularList(Class<E> c, int size) {
		elements = (E[]) Array.newInstance(c, size);
		loc = 0;
	}
	
	public int size() {
		return elements.length;
	}

	public boolean isEmpty() {
		return get(0) == null;
	}
	
	public E getLast() {
		return get(mod(loc - 1));
	}

	public E add(E e) {
		E ret = set(loc, e);
		increment();
		return ret;
	}
	
	private int mod(int n) {
		if (n >= 0)
			return n % elements.length;
		return elements.length- mod(-n - 1) - 1;
	}
	
	private void increment() {
		loc = mod(loc + 1);
	}

	public synchronized E get(int index) {
		return elements[index];
	}
	
	private synchronized E set(int index, E val) {
		E ret = elements[index];
		elements[index] = val;
		return ret;
	}

	public Iterator<E> iterator() {
		return new Iter();
	}
	
	private class Iter implements Iterator<E> {
		
		private int loc;
		
		public Iter() {
			loc = 0;
		}
		
		@Override
		public boolean hasNext() {
			return get(loc) != null && loc < elements.length;
		}

		@Override
		public E next() {
			return get(loc++);
		}
		
	}

}
