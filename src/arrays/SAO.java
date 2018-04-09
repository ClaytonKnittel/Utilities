package arrays;

import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
public class SAO {
	
	public static <T> T[] append(Comparable<T>[] s, Comparable<T> a) {
		return (T[]) AO.append(s, a, indexOf(s, a));
	}
	
	public static <T> T[] appendUnique(Comparable<T>[] s, Comparable<T> a) {
		int pos = indexOf(s, a);
		if (s.length > 0 && pos < s.length) {
			if (s[pos].compareTo((T) a) == 0)
				return (T[]) s;
		}
		return (T[]) AO.append(AO.append(AO.subset(s, 0, pos), a), AO.subset(s, pos, s.length));
	}
	
	public static <T> T[] remove(Comparable<T>[] s, Comparable<T> a) {
		int pos = indexOf(s, a);
		if (pos == s.length)
			return (T[]) s;
		if (!s[pos].equals(a))
			return (T[]) s;
		return (T[]) AO.append(AO.subset(s, 0, pos), AO.subset(s, pos + 1, s.length));
	}
	
	public static <T> boolean intersects(Comparable<T>[] a, Comparable<T>[] b) {
		int ca = 0, cb = 0;
		while (ca < a.length && cb < b.length) {
			int n = a[ca].compareTo((T) b[cb]);
			if (n == 0)
				return true;
			if (n < 0)
				ca ++;
			else
				cb ++;
		}
		return false;
	}
	
	public static <T> T[] intersection(Comparable<T>[] a, Comparable<T>[] b) {
		if (a.length == 0)
			return (T[]) a;
		if (b.length == 0)
			return (T[]) a;
		Comparable<T>[] ret = (Comparable<T>[]) Array.newInstance(a[0].getClass(), 0);
		for (Comparable<T> t : a) {
			if (isIn(b, t))
				ret = (Comparable<T>[]) append(ret, t);
		}
		return (T[]) ret;
	}
	
	public static <T> T[] removeDuplicateVals(T[] a) {
		if (a.length == 0)
			return a;
		T[] ret = (T[]) Array.newInstance(a[0].getClass(), 1);
		ret[0] = a[0];
		for (int x = 1; x < a.length; x++) {
			if (!a[x].equals(ret[ret.length - 1]))
				ret = AO.append(ret, a[x]);
		}
		return ret;
	}
	
	public static <T> boolean equals(T[] a, T[] b) {
		if (a.length != b.length)
			return false;
		for (int x = 0; x < a.length; x++) {
			if (!a[x].equals(b[x]))
				return false;
		}
		return true;
	}
	
	public static <T> boolean isIn(Comparable<T>[] s, Comparable<T> a) {
		if (s.length == 0)
			return false;
		int i = indexOf(s, a);
		if (i == s.length)
			return false;
		return s[indexOf(s, a)].equals(a);
	}
	
	/**
	 * 
	 * @param s an array
	 * @param a an element to find the position of/where it should go
	 * @return where a would fit into the array to keep it ordered
	 */
	public static <T> int indexOf(Comparable<T>[] s, Comparable<T> a) {
		if (s.length == 0)
			return 0;
		return find(s, a, 0, s.length);
	}
	
	private static <T> int find(Comparable<T>[] s, Comparable<T> a, int start, int stop) {
		if (start == stop)
			return start;
		if (stop - start == 1) {
			if (a.compareTo((T) s[start]) > 0)
				return stop;
			return start;
		}
		if (a.compareTo((T) s[(stop + start) / 2]) >= 0)
			return find(s, a, (stop + start) / 2, stop);
		return find(s, a, start, (stop + start) / 2);
	}
	
	/**
	 * much more efficient than mergeSort for very small arrays, less resource intensive
	 * @param a array to be sorted
	 * @return sorted array
	 */
	public static <T> T[] selectionSort(Comparable<T>[] a) {
		if (a.length <= 1)
			return (T[]) a;
		Comparable<T>[] r = AO.clone(a);
		T temp;
		int y;
		next: for (int x = 1; x < a.length; x++) {
			temp = (T) r[x];
			for (y = x - 1; y >= 0; y--) {
				if (r[y].compareTo(temp) <= 0)
					break;
			}
			y++;
			for (; x > y; x--) {
				r[x] = r[x - 1];
			}
			r[y] = (Comparable<T>) temp;
			continue next;
		}
		return (T[]) r;
	}
	
	/**
	 * good for sorting very large arrays
	 * @param a array to be sorted
	 * @return sorted array
	 */
	public static <T> T[] sort(Comparable<T>[] a) {
		if (a.length <= 1)
			return (T[]) a;
		if (a.length == 2) {
			if (a[1].compareTo((T) a[0]) < 0) {
				Comparable<T> c = a[0];
				a[0] = a[1];
				a[1] = c;
			}
			return (T[]) a;
		}
		Comparable<T>[] one = (Comparable<T>[]) sort(AO.subset(a, 0, a.length / 2));
		Comparable<T>[] two = (Comparable<T>[]) sort(AO.subset(a, a.length / 2, a.length));
		Comparable<T>[] ret = (Comparable<T>[]) Array.newInstance(a[0].getClass(), a.length);
		int p1 = 0, p2 = 0;
		for (int x = 0; p1 + p2 < one.length + two.length; x++) {
			if (p1 == one.length) {
				ret[x] = two[p2];
				p2++;
			}
			else if (p2 == two.length) {
				ret[x] = one[p1];
				p1++;
			}
			else {
				if (one[p1].compareTo((T) two[p2]) > 0) {
					ret[x] = two[p2];
					p2++;
				}
				else {
					ret[x] = one[p1];
					p1++;
				}
			}
		}
		return (T[]) ret;
	}
	
}
