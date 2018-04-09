package numbers;

import java.lang.reflect.Array;

import arrays.AO;
import arrays.Pair;

public class TypeCount<T> extends Pair<T, Integer> {
	
	public TypeCount(T t, Integer count) {
		super(t, count);
		if (count < 0)
			throw new IllegalArgumentException("Count must be nonnegative");
	}
	
	public TypeCount(T t) {
		this(t, 1);
	}
	
	public T val() {
		return x();
	}
	
	public int count() {
		return y();
	}
	
	public void add() {
		add(1);
	}
	
	public void add(int n) {
		setY(y() + n);
	}
	
	public void sub() {
		sub(1);
	}
	
	public void sub(int n) {
		if (y() - n < 0)
			throw new InvalidOperationException("Invalid count value: " + (y() - n));
		setY(y() - n);
	}
	
	public void combine(TypeCount<T> t) {
		if (!x().equals(t.x()))
			throw new InvalidOperationException("Cannot combine two unequal types");
		add(t.y());
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		T[] ret = (T[]) Array.newInstance(x().getClass(), y());
		AO.fill(ret, x());
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(TypeCount<T>[] t) {
		int len = 0;
		for (TypeCount<T> q : t)
			len += q.y();
		T[] ret = (T[]) Array.newInstance(t[0].x().getClass(), len);
		int place = 0;
		for (int x = 0; x < t.length; x++) {
			for (int y = 0; y < t[x].y(); y++)
				ret[place++] = t[x].x();
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TypeCount))
			return false;
		return ((TypeCount<T>) o).x().equals(x());
	}
	
}
