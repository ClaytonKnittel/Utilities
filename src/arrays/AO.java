package arrays;

import java.lang.reflect.Array;

import files.FileManager;
import numbers.N;

@SuppressWarnings("unchecked")
public final class AO {
	
	public static <T> T[] reverse(T[] s) {
		if (s.length <= 1)
			return s;
		T[] ret = (T[]) Array.newInstance(s[0].getClass(), s.length);
		for (int x = 0; x < s.length; x++) {
			ret[s.length - 1 - x] = s[x];
		}
		return ret;
	}
	
	public static <T> T[] append(T[] s, T a) {
		T[] ret = (T[]) Array.newInstance(a.getClass(), s.length + 1);
		for (int x = 0; x < s.length; x++)
			ret[x] = s[x];
		ret[s.length] = a;
		return ret;
	}
	
	public static <T> T[] append(T a, T[] s) {
		if (s == null)
			s = (T[]) Array.newInstance(a.getClass(), 0);
		T[] ret = (T[]) Array.newInstance(a.getClass(), s.length + 1);
		ret[0] = a;
		for (int x = 0; x < s.length; x++)
			ret[x + 1] = s[x];
		return ret;
	}
	
	public static <T> T[] append(T[] s, T a, int index) {
		return (T[]) append(append(subset(s, 0, index), a), subset(s, index, s.length));
	}
	
	public static <T> T[] append(T[] s, T[] a) {
		Class<T> c;
		if (s.length == 0 && a.length == 0)
			return s;
		if (s.length == 0)
			c = (Class<T>) a[0].getClass();
		else
			c = (Class<T>) s[0].getClass();
		T[] ret = (T[]) Array.newInstance(c, s.length + a.length);
		for (int x = 0; x < s.length; x++)
			ret[x] = s[x];
		for (int x = 0; x < a.length; x++)
			ret[x + s.length] = a[x];
		return ret;
	}
	
	public static <T> T[][] append2D(T[][] s, T[] a) {
		if (a == null)
			return s;
		if (s == null || s.length == 0) {
			T[][] ret = (T[][]) Array.newInstance((Class<T>) a.getClass(), 1);
			ret[0] = a;
			return ret;
		}
		T[][] ret = (T[][]) Array.newInstance((Class<T>) s[0].getClass(), s.length + 1);
		for (int x = 0; x < s.length; x++) {
			ret[x] = (T[]) Array.newInstance((Class<T>) s[x][0].getClass(), s[x].length);
			for (int y = 0; y < s[x].length; y++) {
				ret[x][y] = s[x][y];
			}
		}
		ret[s.length] = (T[]) Array.newInstance((Class<T>) s[s.length - 1][0].getClass(), a.length);
		for (int y = 0; y < a.length; y++) {
			ret[s.length][y] = a[y];
		}
		return ret;
	}
	
	public static <T> T[][] append2D(T[][] s, T[][] a) {
		if (a == null)
			return s;
		if (s == null)
			return a;
		if (a.length == 0)
			return s;
		if (s.length == 0)
			return a;
		T[][] ret = (T[][]) Array.newInstance(s[0].getClass(), s.length + a.length);
		for (int x = 0; x < s.length; x++) {
			ret[x] = (T[]) Array.newInstance((Class<T>) s[x][0].getClass(), s[x].length);
			for (int y = 0; y < s[x].length; y++) {
				ret[x][y] = s[x][y];
			}
		}
		for (int x = s.length; x < ret.length; x++) {
			ret[x] = (T[]) Array.newInstance((Class<T>) a[x - s.length][0].getClass(), a[x - s.length].length);
			for (int y = 0; y < a[x - s.length].length; y++) {
				ret[x][y] = a[x - s.length][y];
			}
		}
		return ret;
	}
	
	public static <T> T[] appendUnique(T[] s, T a) {
		for (T t : s) {
			if (t.equals(a))
				return s;
		}
		return append(s, a);
	}
	
	public static <T> T[] appendUnique(T[] s, T[] a) {
		for (T t : a) {
			s = appendUnique(s, t);
		}
		return s;
	}
	
	public static <T> T[] remove(T[] a, T b) {
		for (int x = 0; x < a.length; x++) {
			if (a[x].equals(b))
				return append(subset(a, 0, x), subset(a, x + 1, a.length));
		}
		return a;
	}
	
	public static <T> T[] removeEl(T[] a, int index) {
		return append(subset(a, 0, index), subset(a, index + 1, a.length));
	}
	
	public static <T> T[] subset(T[] s, int start, int stop) {
		if (s.length == 0)
			return s;
		if (stop < start)
			return (T[]) Array.newInstance(s[0].getClass(), 0);
		T[] ret = (T[]) Array.newInstance(s[0].getClass(), stop - start);
		for (int x = 0; x < ret.length; x++) {
			ret[x] = s[x + start];
		}
		return ret;
	}
	
	public static <T> T[] subset(T[] s, int start) {
		return subset(s, start, s.length);
	}
	
	public static <T> T[] randomize(T[] s) {
		T[] take = clone(s);
		T[] from = (T[]) Array.newInstance(s[0].getClass(), s.length);
		int pick;
		for (int x = 0; x < from.length; x++) {
			pick = (int) (take.length * Math.random());
			from[x] = take[pick];
			take = (T[]) removeEl(take, pick);
		}
		return from;
	}
	
	public static <T> T[] permutation(T[] s, long permutation) {
		return lexiperm(s, permutation, s.length);
	}
	
	public static <T> T[] permutation(T[] s, long permutation, int permSize) {
		return lexiperm(s, permutation, permSize);
	}
	
	public static <T> long numPerms(int size, int permSize) {
		return N.factorial(size) / N.factorial(size - permSize);
	}
	
	private static <T> T[] lexiperm(T[] s, long permutation, int len) {
		Integer[] r = new Integer[len];
		for (int x = 0; x < len; x++)
			r[x] = -1;
		for (int x = 0; x < len; x++) {
			r[x] = next(r, (permutation * N.factorial(s.length - len) / N.factorial(s.length - 1 - x)) % (s.length - x));
		}
		
		T[] ret = (T[]) Array.newInstance(s[0].getClass(), len);
		for (int x = 0; x < ret.length; x++)
			ret[x] = s[r[x]];
		return ret;
	}
	
	private static int next(Integer[] n, long num) {
		for (int x = 0;; x++) {
			if (!AO.isIn(n, x)) {
				if (num == 0)
					return x;
				num --;
			}
		}
	}
	
	public static <T> boolean isIn(T[] s, T o) {
		for (T t : s) {
			if (t.equals(o))
				return true;
		}
		return false;
	}
	
	public static boolean isIn(boolean[] b, boolean a) {
		for (boolean c : b)
			if (c == a)
				return true;
		return false;
	}
	
	public static <T> int indexFirst(T[] s, T a) {
		for (int x = 0; x < s.length; x++) {
			if (s[x].equals(a))
				return x;
		}
		return -1;
	}
	
	public static <T> int indexLast(T[] s, T a) {
		int x = s.length - 1;
		for (; x >= 0; x--) {
			if (s[x].equals(a))
				break;
		}
		return x;
	}
	
	public static <T> void fill(T[] s, T a) {
		for (int x = 0; x < s.length; x++)
			s[x] = a;
	}
	
	public static <T> void fill(T[][] s, T a) {
		for (int x = 0; x < s.length; x++) {
			for (int y = 0; y < s[x].length; y++)
				s[x][y] = a;
		}
	}
	
	public static int[] fill(int[] s, int a) {
		for (int x = 0; x < s.length; x++)
			s[x] = a;
		return s;
	}
	
	public static int sum(int[] s) {
		int sum = 0;
		for (int a : s)
			sum += a;
		return sum;
	}
	
	public static int sum(Integer[] s) {
		int sum = 0;
		for (Integer a : s)
			sum += a;
		return sum;
	}
	
	public static int product(int[] s) {
		int prod = 1;
		for (int a : s)
			prod *= a;
		return prod;
	}
	
	public static int product(Integer[] s) {
		int prod = 1;
		for (Integer a : s)
			prod *= a;
		return prod;
	}
	
	public static <T> T getFirstIntersectingValue(T[] a, T[] b) {
		for (T x: a) {
			for (T y: b) {
				if (x.equals(y))
					return x;
			}
		}
		return null;
	}
	
	public static <T> boolean intersects(T[] a, T[] b) {
		return !(getFirstIntersectingValue(a, b) == null);
	}
	
	public static <T> T[] intersection(T[] a, T[] b) {
		if (a.length == 0)
			return a;
		if (b.length == 0)
			return a;
		T[] ret = (T[]) Array.newInstance(a[0].getClass(), 0);
		for (T t : a) {
			if (isIn(b, t))
				ret = append(ret, t);
		}
		return ret;
	}
	
	public static <T> T[] minus(T[] a, T[] b) {
		T[] ret = clone(a);
		for (T t : b) {
			ret = AO.remove(ret, t);
		}
		return ret;
	}
	
	public static <T> String toString(T[] a) {
		return toString(a, " ");
	}
	
	public static <T> String toString(T[] a, String separator) {
		String ret = "";
		if (a != null) {
			for (T s : a)
				ret += s + separator;
		}
		return ret;
	}
	
	public static <T> void print(T[] a) {
		print(a, " ");
	}
	
	public static <T> void print(T[] a, String separator) {
		System.out.println(toString(a, separator));
	}
	
	public static <T> void print(int[] a) {
		print(a, " ");
	}
	
	public static <T> void print(int[] a, String separator) {
		for (int b : a)
			System.out.print(b + separator);
		if (!separator.equals("\n"))
			System.out.println();
	}
	
	public static void print(boolean[] a) {
		for (boolean s : a)
			System.out.print(s ? "1" : "0");
		System.out.println();
	}
	
	public static <T> void println(T[] a) {
		print(a, "\n");
	}
	
	public static <T> String toString2D(T[][] a) {
		return toString2D(a, " ");
	}
	
	public static <T> String toString2D(T[][] a, String delimiter) {
		String ret = "";
		for (T[] s : a) {
			ret += toString(s, delimiter) + "\n";
		}
		return ret.substring(0, ret.length() - 2);
	}
	
	public static <T> void print2D(T[][] a) {
		print2D(a, " ");
	}
	
	public static <T> void print2D(T[][] a, String delimiter) {
		for (T[] s : a) {
			print(s, delimiter);
		}
	}
	
	public static void print2D(boolean[][] a) {
		print2D(a, "");
	}
	
	public static void print2D(boolean[][] a, String delimiter) {
		for (boolean[] s : a) {
			print(s, delimiter);
		}
	}
	
	public static void print2D(int[][] a) {
		print2D(a, " ");
	}
	
	public static void print2D(int[][] a, String delimiter) {
		for (int[] i : a) {
			for (int j : i)
				System.out.print(j + delimiter);
			System.out.println();
		}
	}
	
	public static void print2D(float[][] a) {
		print2D(a, "");
	}
	
	public static void print2D(float[][] a, String delimiter) {
		for (float[] s : a) {
			print(s, delimiter);
		}
	}
	
	public static void print(boolean[] a, String delimiter) {
		for (boolean b : a)
			System.out.print((b ? 1 : 0) + delimiter);
		System.out.println();
	}
	
	public static void print(float[] a) {
		print(a, "\t");
	}
	
	public static void print(float[] a, String delimiter) {
		for (float b : a)
			System.out.print(b + delimiter);
		System.out.println();
	}
	
	public static <T> boolean equals(T[] a, T[] b) {
		next: for (T i : a) {
			for (T j : b) {
				if (i.equals(j)) {
					b = remove(b, j);
					continue next;
				}
			}
			return false;
		}
		return b.length == 0;
	}
	
	public static <T> boolean equals2(Comparable<T>[] a, Comparable<T>[] b) {
		if (a.length != b.length)
			return false;
		T[] aa = SAO.selectionSort(a);
		T[] bb = SAO.selectionSort(b);
		for (int x = 0; x < aa.length; x++) {
			if (!aa[x].equals(bb[x]))
				return false;
		}
		return true;
	}
	
	public static <T> T max(Comparable<T>[] a) {
		if (a.length == 0)
			return null;
		Comparable<T> max = a[0];
		for (Comparable<T> el : a) {
			if (el.compareTo((T) max) > 0)
				max = el;
		}
		return (T) max;
	}
	
	public static <T> T min(Comparable<T>[] a) {
		if (a.length == 0)
			return null;
		Comparable<T> max = a[0];
		for (Comparable<T> el : a) {
			if (el.compareTo((T) max) < 0)
				max = el;
		}
		return (T) max;
	}
	
	public static <T> T[] clone(T[] b) {
		if (b == null)
			return b;
		T[] t = (T[]) Array.newInstance(b[0].getClass(), b.length);
		System.arraycopy(b, 0, t, 0, b.length);
		return t;
	}
	
	public static <T> T[][] clone(T[][] b) {
		if (b == null)
			return b;
		T[][] t = (T[][]) Array.newInstance(b[0].getClass(), b.length);
		for (int x = 0; x < b.length; x++) {
			t[x] = (T[]) Array.newInstance(b[x][0].getClass(), b[x].length);
			System.arraycopy(b[x], 0, t[x], 0, b[x].length);
		}
		return t;
	}
	
	public static boolean[] clone(boolean[] b) {
		boolean[] c = new boolean[b.length];
		System.arraycopy(b, 0, c, 0, b.length);
		return c;
	}
	
	public static boolean[][] clone(boolean[][] b) {
		if (b.length == 0)
			return b;
		boolean[][] c = new boolean[b.length][b[0].length];
		for (int x = 0; x < b.length; x++)
			System.arraycopy(b[x], 0, c[x], 0, b[x].length);
		return c;
	}
	
	public static void save(int[] a, String location) {
		String[] info = new String[a.length];
		for (int x = 0; x < a.length; x++)
			info[x] = "" + a[x];
		FileManager.save(info, location);
	}
	
	public static Integer[] load(String location) {
		return toIntArray(FileManager.toString(location));
	}
	
	public static Integer[] load(String location, int toIndex) {
		return toIntArray(FileManager.toString(location, toIndex));
	}
	
	public static Integer[] loadPart(String location, int el) {
		return toIntArray(FileManager.toString(location, "" + el));
	}
	
	static Integer[] toIntArray(String[] s) {
		Integer[] ret = new Integer[s.length];
		for (int x = 0; x < s.length; x++)
			ret[x] = Integer.parseInt(s[x]);
		return ret;
	}
	
	public static void save(Integer[] a, String location) {
		String[] info = new String[a.length];
		for (int x = 0; x < a.length; x++)
			info[x] = "" + a[x];
		FileManager.save(info, location);
	}
	
}
