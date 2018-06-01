package structures;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.HashMap;

import numbers.InvalidOperationException;

/**
 * A max-heap implementation
 * 
 * @author claytonknittel
 *
 * @param <T>
 */
public class Heap<T> {
	
	private Comparator<T> c;
	private Spot[] heap;
	private HashMap<T, Spot> spots;
	
	private int loc;
	
	@SuppressWarnings("unchecked")
	public Heap(int size, T exampleEl, Comparator<T> c) {
		this.c = c;
		heap = (Spot[]) Array.newInstance(new Spot(exampleEl, 0).getClass(), size + 1);
		spots = new HashMap<>(size);
		loc = 1;
	}
	
	@SuppressWarnings("unchecked")
	public Heap(T[] els, Comparator<T> c) {
		this.c = c;
		heap = (Spot[]) Array.newInstance(new Spot(els[0], 0).getClass(), els.length + 1);
		for (int i = 0; i < els.length; i++) {
			heap[i + 1] = new Spot(els[i], i + 1);
			spots.put(els[i], heap[i + 1]);
		}
		spots = new HashMap<>(els.length);
		loc = 1;
	}
	
	public int size() {
		return loc - 1;
	}
	
	public boolean isEmpty() {
		return loc == 1;
	}
	
	public int depth() {
		return (int) Math.ceil(Math.log(loc - 1) / Math.log(2)) + 1;
	}
	
	public void add(T t) {
		if (loc >= heap.length)
			throw new InvalidOperationException("Cannot add more than " + (heap.length - 1) + " elements to this heap");
		heap[loc] = new Spot(t, loc);
		spots.put(t, heap[loc]);
		decrease(loc++);
	}
	
	/**
	 * To be called whenever the value of an object is decreased
	 * @param loc
	 */
	public void decrease(int loc) {
		if (loc <= 1)
			return;
		int parent = loc / 2;
		if (c.compare(heap[parent].val, heap[loc].val) >= 0)
			return;
		swap(loc, parent);
		decrease(parent);
	}
	
	public void decrease(T s) {
		decrease(spots.get(s).loc);
	}
	
	public T extractMax() {
		if (loc == 1)
			throw new java.lang.RuntimeException("Cannot extract min from an empty heap");
		Spot min = heap[1];
		heap[1] = null;
		swap(1, --loc);
		increase(1);
		return min.val;
	}
	
	private void swap(int s1, int s2) {
		Spot t = heap[s1];
		heap[s1] = heap[s2];
		heap[s2] = t;
		
		if (loc > s2)
			t.loc = s2;
		if (loc > s1)
			heap[s1].loc = s1;
	}
	
	/**
	 * To be called whenever the value of an object increases
	 * @param loc
	 */
	public void increase(int loc) {
		int p1 = loc * 2;
		int p2 = p1 + 1;
		if (p2 < this.loc) {
			if (c.compare(heap[p1].val, heap[loc].val) <= 0 && c.compare(heap[p2].val, heap[loc].val) <= 0)
				return;
			if (c.compare(heap[p1].val, heap[p2].val) > 0) {
				swapLeft(loc);
				increase(p1);
			}
			else {
				swapRight(loc);
				increase(p2);
			}
		} else if (p1 >= this.loc)
			return;
		if (c.compare(heap[p1].val, heap[loc].val) > 0)
			swapLeft(loc);
	}
	
	private void swapLeft(int loc) {
		swap(loc, loc * 2);
	}
	
	private void swapRight(int loc) {
		swap(loc, loc * 2 + 1);
	}
	
	private class Spot {
		T val;
		int loc;
		
		Spot(T val, int loc) {
			this.val = val;
			this.loc = loc;
		}
		
		public String toString() {
			return val + "";
		}
	}
	
	
	public String toString() {
		String ret = "";
		int depth = depth();
		int tabs = (int) Math.pow(2, depth - 1) - 1;
		int start = 1, finish = 2;
		
		while (start < heap.length) {
			ret += nTabs(tabs);
			for (int i = start; i < finish && i < loc; i++)
				ret += heap[i] + nTabs((tabs + 1) * 2);
			ret += "\n";
			tabs /= 2;
			
			start = finish;
			finish *= 2;
		}
		
		return ret;
	}
	
	private static String nTabs(int n) {
		String ret = "";
		for (int i = 0; i < n; i++)
			ret += "\t";
		return ret;
	}
	
	
	
}
