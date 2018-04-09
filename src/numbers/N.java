package numbers;

import java.math.BigInteger;

import arrays.AO;
import arrays.SAO;

public class N {
	
	private static Integer[] primes;
	
	public static final String PRIMES_LOCATION = "/Users/claytonknittel/Documents/workspace/Utilities/data/primes";
	
	public static void createPrimes(int maxPrime) {
		while (!prime(maxPrime))
			maxPrime--;
		primes = AO.loadPart(PRIMES_LOCATION, maxPrime);
		if (primes.length == 0)
			primes = new Integer[] {2, 3};
		else if (primes[primes.length - 1] >= maxPrime)
			return;
		next: for (int x = primes[primes.length - 1] + 2; x < maxPrime; x += 2) {
			for (Integer i : primes) {
				if (x % i == 0)
					continue next;
			}
			primes = AO.append(primes, x);
		}
		AO.save(primes, PRIMES_LOCATION);
	}
	
	public static Integer[] primes() {
		return primes;
	}
	
	public static boolean aPrime(int n) {
		return SAO.isIn(primes, n);
	}
	
	public static boolean prime(int n) {
		if (n <= 1)
			return false;
		if (n == 2)
			return true;
		if ((n & 1) == 0)
			return false;
		for (int x = 3; x * x <= n; x++) {
			if (n % x == 0)
				return false;
		}
		return true;
	}
	
	public static boolean prime(long n) {
		if (n <= 1)
			return false;
		if (n == 2)
			return true;
		if ((n & 1) == 0)
			return false;
		for (int x = 3; x * x <= n; x++) {
			if (n % x == 0)
				return false;
		}
		return true;
	}
	
	public static boolean prime(Longer n) {
		if (n.lessOrEqual(1))
			return false;
		if (n.equals(new Longer(2)))
			return true;
		if (Longer.and(n, Longer.one).equals(Longer.zero))
			return false;
		for (int x = 3; Longer.pow(new Longer(x), new Longer(2)).lessOrEqual(n); x++) {
			if (Longer.mod(n, x).equals(Longer.zero))
				return false;
		}
		return true;
	}
	
	public static boolean coPrime(int a, int b) {
		if (a == 1 && b > 1)
			return true;
		if (b == 1 && a > 1)
			return true;
		if (a % b == 0)
			return false;
		if (b % a == 0)
			return false;
		for (int x = 2; x * x <= a && x * x <= b; x++) {
			if (a % x == 0) {
				if (b % x == 0)
					return false;
				a /= x;
				while (a % x == 0)
					a /= x;
			}
			else {
				while (b % x == 0)
					b /= x;
			}
		}
		if (a == 1)
			return true;
		if (b == 1)
			return true;
		if (a % b == 0)
			return false;
		if (b % a == 0)
			return false;
		return true;
	}
	
	public static Integer[] primeFactors(int n) {
		Integer[] ret = new Integer[0];
		for (int x = 2; n > 1; x++) {
			while (n % x == 0) {
				n /= x;
				ret = AO.append(ret, x);
			}
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static TypeCount<Integer>[] primeFactorPairs(int n) {
		TypeCount<Integer>[] ret = (TypeCount<Integer>[]) new TypeCount<?>[0];
		for (int x = 2; n > 1; x++) {
			if (n % x == 0) {
				n /= x;
				ret = AO.append(ret, new TypeCount<Integer>(x));
			}
			while (n % x == 0) {
				n /= x;
				ret[ret.length - 1].add();
			}
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static TypeCount<Long>[] primeFactorPairs(long n) {
		TypeCount<Long>[] ret = (TypeCount<Long>[]) new TypeCount<?>[0];
		for (long x = 2; n > 1; x++) {
			if (n % x == 0) {
				n /= x;
				ret = AO.append(ret, new TypeCount<Long>(x));
			}
			while (n % x == 0) {
				n /= x;
				ret[ret.length - 1].add();
			}
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static TypeCount<Longer>[] primeFactorPairs(Longer n) {
		TypeCount<Longer>[] ret = (TypeCount<Longer>[]) new TypeCount<?>[0];
		for (Longer x = new Longer(2); n.greater(Longer.one); x.add(1)) {
			if (Longer.mod(n, x).equals(Longer.zero)) {
				ret = AO.append(ret, new TypeCount<Longer>(new Longer(x)));
			}
			while (Longer.mod(n, x).equals(Longer.zero)) {
				n.div(x);
				ret[ret.length - 1].add();
			}
		}
		return ret;
	}
	
	public static Integer[] uniquePrimeFactors(int n) {
		Integer[] ret = new Integer[0];
		for (int x = 2; n > 1; x++) {
			if (n % x == 0)
				ret = AO.append(ret, x);
			while (n % x == 0) {
				n /= x;
			}
		}
		return ret;
	}
	
	public static Integer[] properFactors(int n) {
		if (n == 1)
			return new Integer[] {};
		Integer[] f = new Integer[0];
		for (int x = 1; x <= n / 2; x++) {
			if (n % x == 0)
				f = AO.append(f, x);
		}
		return f;
	}
	
	public static Integer[] factors(int n) {
		TypeCount<Integer>[] i = primeFactorPairs(n);
		Integer[] ret = new Integer[len(i)];
		ffactors(i, i.length - 1, 0, ret, 1);
		return SAO.sort(ret);
	}
	
	public static Long[] factors(long n) {
		TypeCount<Long>[] i = primeFactorPairs(n);
		Long[] ret = new Long[len(i)];
		ffactors(i, i.length - 1, 0, ret, 1);
		return SAO.sort(ret);
	}
	
	public static Longer[] factors(Longer n) {
		TypeCount<Longer>[] i = primeFactorPairs(n);
		Longer[] ret = new Longer[len(i)];
		ffactors(i, i.length - 1, 0, ret, new Longer(Longer.one));
		return SAO.sort(ret);
	}
	
	private static int ffactors(TypeCount<Integer>[] p, int pos, Integer l, Integer[] ret, int mult) {
		if (l == ret.length)
			return l;
		if (pos == -1) {
			ret[l++] = mult;
			return l;
		}
		int m = 1;
		for (int x = 0; x <= p[pos].count(); x++) {
			l = ffactors(p, pos - 1, l, ret, mult * m);
			m *= p[pos].val();
		}
		return l;
	}
	
	private static int ffactors(TypeCount<Long>[] p, int pos, int l, Long[] ret, long mult) {
		if (l == ret.length)
			return l;
		if (pos == -1) {
			ret[l++] = mult;
			return l;
		}
		int m = 1;
		for (int x = 0; x <= p[pos].count(); x++) {
			l = ffactors(p, pos - 1, l, ret, mult * m);
			m *= p[pos].val();
		}
		return l;
	}
	
	private static int ffactors(TypeCount<Longer>[] p, int pos, int l, Longer[] ret, Longer mult) {
		if (l == ret.length)
			return l;
		if (pos == -1) {
			ret[l++] = mult;
			return l;
		}
		Longer m = new Longer(1);
		for (int x = 0; x <= p[pos].count(); x++) {
			l = ffactors(p, pos - 1, l, ret, Longer.mult(mult, m));
			m.mult(p[pos].val());
		}
		return l;
	}
	
	private static int len(TypeCount<? extends Number>[] primeFactors) {
		int ret = 1;
		for (int x = 0; x < primeFactors.length; x++)
			ret *= 1 + primeFactors[x].count();
		return ret;
	}
	
	public static Integer[] factorsOver(int n, int min) {
		Integer[] f = new Integer[0];
		for (int x = min; x <= n / 2; x++) {
			if (n % x == 0)
				f = AO.append(f, x);
		}
		if (n >= min)
			f = AO.append(f, n);
		return f;
	}
	
	public static int lcd(int a, int b) {
		if (a == 0)
			return b;
		if (b == 0)
			return a;
		int x = Math.min(a, b);
		for (; x > 1; x--)
			if (a % x == 0 && b % x == 0)
				break;
		return x;
	}
	
	public static int numFactors(int n) {
		int ret = (n == 1 ? 1 : 2);
		for (int x = 2; x <= n / 2; x++) {
			if (n % x == 0)
				ret ++;
		}
		return ret;
	}
	
	public static int numFactors(long n) {
		int ret = (n == 1 ? 1 : 2);
		for (int x = 2; x <= n / 2; x++) {
			if (n % x == 0)
				ret ++;
		}
		return ret;
	}
	
	public static int numFactors(Longer n) {
		return factors(n).length;
	}
	
	public static Integer[][] productPartitions(int n) {
		return allFactors(n, 2);
	}
	
	private static Integer[][] allFactors(int num, int max) {
		if (num < max && num > 1) {
			return null;
		}
		Integer[][] ret = new Integer[0][0];
		for (Integer a : N.factorsOver(num, max))
			ret = AO.append2D(ret, elAtBegin(a, allFactors(num / a, a)));
		return ret;
	}
	
	private static Integer[][] elAtBegin(int el, Integer[][] a) {
		if (a == null)
			return new Integer[0][0];
		if (a.length == 0)
			return new Integer[][] {{el}};
		Integer[][] ret = new Integer[a.length][0];
		for (int x = 0; x < ret.length; x++) {
			ret[x] = AO.append(a[x], el);
		}
		return ret;
	}
	
	
	public static long factorial(long l) {
		if (l < 0)
			return 0;
		if (l < 2)
			return 1;
		return l * factorial(l - 1);
	}
	
	public static BigInteger factorial(BigInteger b) {
		if (b.compareTo(BigInteger.ZERO) == -1)
			return BigInteger.ZERO;
		if (b.compareTo(BigInteger.valueOf(2)) == -1)
			return BigInteger.ONE;
		BigInteger res = BigInteger.ONE;
		for (; b.compareTo(BigInteger.ZERO) == 1; b = b.subtract(BigInteger.ONE))
			res = res.multiply(b);
		return res;
	}
	
	public static long factorial(long l, long div) {
		if (l < div)
			return 0;
		if (l == div)
			return 1;
		return l * factorial(l - 1, div);
	}
	
	public static Longer factorial(Longer l) {
		if (l.sign())
			return new Longer(Longer.zero);
		if (l.less(new Longer(2)))
			return new Longer(Longer.one);
		return Longer.mult(l, factorial(Longer.sub(l, 1)));
	}
	
	public static Longer factorial(Longer l, Longer div) {
		if (l.less(div))
			return new Longer(Longer.zero);
		if (l.equals(div))
			return new Longer(Longer.one);
		return Longer.mult(l, factorial(Longer.sub(l, 1)));
	}
	
	public static int nCr(int n, int r) {
		return (int) (factorial(n, n - r) / factorial(r));
	}
	
	public static long nCr(long n, long r) {
		return factorial(n, n - r) / factorial(r);
	}
	
	public static Longer nCr(Longer n, Longer r) {
		return Longer.div(factorial(n, Longer.sub(n, r)), factorial(r));
	}
	
	public static boolean[][] nCrList(int n, int r) {
		boolean[][] ret = new boolean[nCr(n, r)][n];
		getCombos(ret, new Counter(), -1, r);
		return ret;
	}
	
	private static void getCombos(boolean[][] ret, Counter loc, int locLast, int r) {
		if (r == 0) {
			loc.count();
			return;
		}
		int rem;
		for (int x = locLast + 1; x <= ret[0].length - r; x++) {
			rem = loc.get();
			getCombos(ret, loc, x, r - 1);
			while (rem < loc.get()) {
				ret[rem++][x] = true;
			}
		}
	}
	
	private static class Counter {
		int count;
		
		private Counter() {
			count = 0;
		}
		
		private void count() {
			count++;
		}
		
		private int get() {
			return count;
		}
	}
	
	public static int nPr(int n, int r) {
		return (int) factorial(n, n - r);
	}
	
	public static long nPr(long n, long r) {
		return factorial(n, n - r);
	}
	
	public static Longer nPr(Longer n, Longer r) {
		return factorial(n, Longer.sub(n, r));
	}
	
	public static int digit(int l, int digit) {
		for (int x = 0; x < digit; x++)
			l /= 10;
		return l % 10;
	}
	
	public static int digit(long l, int digit) {
		for (int x = 0; x < digit; x++)
			l /= 10;
		return (int) (l % 10);
	}
	
	public static int digit(Longer l, int digit) {
		Longer k = new Longer(l);
		for (int x = 0; x < digit; x++)
			k.div(10);
		return Longer.mod(k, 10).intValue();
	}
	
	public static int digit(BigInteger a, int digit) {
		for (int x = 0; x < digit; x++)
			a = a.divide(BigInteger.TEN);
		return a.mod(BigInteger.TEN).intValue();
	}
	
	public static Integer[] toArray(Integer a) {
		Integer[] ret = new Integer[("" + a).length()];
		for (int x = 0; x < ret.length; x++)
			ret[x] = digit(a, ret.length - 1 - x);
		return ret;
	}
	
	public static Integer[] toArray(Long a) {
		Integer[] ret = new Integer[("" + a).length()];
		for (int x = 0; x < ret.length; x++)
			ret[x] = digit(a, ret.length - 1 - x);
		return ret;
	}
	
	public static Integer[] toArray(Longer a) {
		Integer[] ret = new Integer[("" + a).length()];
		for (int x = 0; x < ret.length; x++)
			ret[x] = digit(a, ret.length - 1 - x);
		return ret;
	}
	
	public static Integer[] toArray(BigInteger a) {
		Integer[] ret = new Integer[("" + a).length()];
		for (int x = 0; x < ret.length; x++)
			ret[x] = digit(a, ret.length - 1 - x);
		return ret;
	}
	
	public static int[] toArray(int a) {
		int[] ret = new int[("" + a).length()];
		for (int x = 0; x < ret.length; x++)
			ret[x] = digit(a, ret.length - 1 - x);
		return ret;
	}
	
	public static int toInt(Integer[] a) {
		int ret = 0;
		for (Integer s : a)
			ret = ret * 10 + s;
		return ret;
	}
	
	public static long toLong(Integer[] a) {
		long ret = 0;
		for (Integer s : a)
			ret = ret * 10 + s;
		return ret;
	}
	
	public static int toInt(int[] a) {
		int ret = 0;
		for (int s : a)
			ret = ret * 10 + s;
		return ret;
	}
	
	public static long toLong(int[] a) {
		long ret = 0;
		for (int s : a)
			ret = ret * 10 + s;
		return ret;
	}
	
	public static BigInteger toBigInteger(Integer[] a) {
		BigInteger ret = BigInteger.ZERO;
		for (int s : a)
			ret = ret.multiply(BigInteger.TEN).add(BigInteger.valueOf(s));
		return ret;
	}
	
	public static BigInteger toBigInteger(int[] a) {
		BigInteger ret = BigInteger.ZERO;
		for (int s : a)
			ret = ret.multiply(BigInteger.TEN).add(BigInteger.valueOf(s));
		return ret;
	}
	
	public static boolean[] binary(int a) {
		return binary(a, bitsInNum(a));
	}
	
	public static boolean[] binary(int a, int len) {
		boolean[] ret = new boolean[len];
		for (int x = 0; x < ret.length; x++) {
			ret[x] = (a / pow2(ret.length - 1 - x)) % 2 == 1;
		}
		return ret;
	}
	
	public static boolean[] binary(long a) {
		return binary(a, bitsInNum(a));
	}
	
	public static boolean[] binary(long a, int len) {
		boolean[] ret = new boolean[len];
		for (int x = 0; x < ret.length; x++) {
			ret[x] = (a / pow2(ret.length - 1 - x)) % 2 == 1;
		}
		return ret;
	}
	
	private static long pow2(int pow) {
		if (pow == 0)
			return 1;
		return 2 * pow2(pow - 1);
	}
	
	public static boolean[] bitData(int a) {
		return bitData((long) a);
	}
	
	public static boolean[] bitData(int a, int len) {
		return bitData((long) a, len);
	}
	
	public static boolean[] bitData(long a) {
		return bitData(a, bitsInNum(a));
	}
	
	public static boolean[] bitData(long a, int len) {
		boolean[] bin, ret;
		if (a < 0) {
			bin = binary(-a, len);
			ret = new boolean[bin.length + 1];
			ret[0] = true;
			for (int x = 0; x < bin.length; x++) {
				ret[x + 1] = bin[x];
			}
		} else {
			bin = binary(a, len);
			ret = new boolean[bin.length + 1];
			for (int x = 0; x < bin.length; x++) {
				ret[x + 1] = bin[x];
			}
		}
		return ret;
	}
	
	public static int bitsInNum(int num) {
		return roof(Math.log(num + 1) / Math.log(2));
	}
	
	public static int bitsInNum(long num) {
		return roof(Math.log(Math.abs(num) + 1) / Math.log(2));
	}
	
	public static int bitsToNumLen(int numBits) {
		return roof(numBits * Math.log(2) / Math.log(10));
	}
	
	public static int roof(double a) {
		if (a == (int) a)
			return (int) a;
		return (int) a + 1;
	}
	
	public static int length(int a) {
		return ("" + a).length();
	}
	
	public static int length(long a) {
		return ("" + a).length();
	}
	
	public static int length(Longer a) {
		return ("" + a).length();
	}
	
	public static int length(BigInteger a) {
		return ("" + a).length();
	}
	
	public static int sqrt(int n) {
		return (int) Math.sqrt(n);
	}
	
	
	static final Integer[] validLastDigits = new Integer[] {
			1, 4, 5, 6, 9
	};
	static final Integer[] validDigitalRoots = new Integer[] {
			1, 4, 7, 9
	};
	
	public static boolean square(int n) {
		int lastDigit = n % 10;
		if (!SAO.isIn(validLastDigits, lastDigit)) {
			int ntz = 0;
			while (n % 10 == 0) {
				n /= 10;
				ntz ++;
			}
			if (ntz == 0 || (ntz & 1) == 1)
				return false;
			lastDigit = n % 10;
		}
		if (!SAO.isIn(validDigitalRoots, digitalRoot(n)))
			return false;
		int tensDigit = (n % 100) / 10;
		if (lastDigit == 5) {
			if (tensDigit != 2)
				return false;
		}
		if (lastDigit == 6) {
			if ((tensDigit & 1) == 0)
				return false;
		} else {
			if ((tensDigit & 1) == 1)
				return false;
		}
		int rSquared8 = (int) ((((long) n) * n) % 8);
		if (n % 4 == 0) {
			if (rSquared8 != 0)
				return false;
		} else {
			if ((n & 1) == 0) {
				if (rSquared8 != 4)
					return false;
			} else {
				if (rSquared8 != 1)
					return false;
			}
		}
		if ((numFactors(n) & 1) == 0)
			return false;
		return true;
	}
	
	public static boolean square(long n) {
		int lastDigit = (int) (n % 10);
		if (!SAO.isIn(validLastDigits, lastDigit)) {
			int ntz = 0;
			while (n % 10 == 0) {
				n /= 10;
				ntz ++;
			}
			if (ntz == 0 || (ntz & 1) == 1)
				return false;
			lastDigit = (int) (n % 10);
		}
		if (!SAO.isIn(validDigitalRoots, digitalRoot(n)))
			return false;
		int tensDigit = (int) ((n % 100) / 10);
		if (lastDigit == 5) {
			if (tensDigit != 2)
				return false;
		}
		if (lastDigit == 6) {
			if ((tensDigit & 1) == 0)
				return false;
		} else {
			if ((tensDigit & 1) == 1)
				return false;
		}
		int rSquared8 = (int) ((((long) n) * n) % 8);
		if (n % 4 == 0) {
			if (rSquared8 != 0)
				return false;
		} else {
			if ((n & 1) == 0) {
				if (rSquared8 != 4)
					return false;
			} else {
				if (rSquared8 != 1)
					return false;
			}
		}
		if ((numFactors(n) & 1) == 0)
			return false;
		return true;
	}
	
	public static boolean square(Longer n) {
		int lastDigit = Longer.mod(n, 10).intValue();
		if (!SAO.isIn(validLastDigits, lastDigit)) {
			int ntz = 0;
			while (Longer.mod(n, 10).equals(Longer.zero)) {
				n.div(10);
				ntz ++;
			}
			if (ntz == 0 || (ntz & 1) == 1)
				return false;
			lastDigit = Longer.mod(n, 10).intValue();
		}
		if (!SAO.isIn(validDigitalRoots, digitalRoot(n)))
			return false;
		int tensDigit = Longer.div(Longer.mod(n, 100), 10).intValue();
		if (lastDigit == 5) {
			if (tensDigit != 2)
				return false;
		}
		if (lastDigit == 6) {
			if ((tensDigit & 1) == 0)
				return false;
		} else {
			if ((tensDigit & 1) == 1)
				return false;
		}
		int rSquared8 = Longer.mod(Longer.mult(n, n), 8).intValue();
		if (Longer.mod(n, 4).equals(Longer.zero)) {
			if (rSquared8 != 0)
				return false;
		} else {
			if (Longer.even(n)) {
				if (rSquared8 != 4)
					return false;
			} else {
				if (rSquared8 != 1)
					return false;
			}
		}
		if (prime(n))
			return false;
		if ((numFactors(n) & 1) == 0) {
			return false;
		}
		return true;
	}
	
	public static int digitalRoot(int n) {
		int sum = 0;
		for (int x = 0; x < N.length(n); x++) {
			sum += digit(n, x);
		}
		if (sum < 10)
			return sum;
		return digitalRoot(sum);
	}
	
	public static int digitalRoot(long n) {
		int sum = 0;
		for (int x = 0; x < N.length(n); x++) {
			sum += digit(n, x);
		}
		if (sum < 10)
			return sum;
		return digitalRoot(sum);
	}
	
	public static int digitalRoot(Longer n) {
		int sum = 0;
		for (int x = 0; x < N.length(n); x++) {
			sum += digit(n, x);
		}
		if (sum < 10)
			return sum;
		return digitalRoot(sum);
	}
	
	public static int numTrailingZeros(int n) {
		int count = 0;
		while (n % 10 == 0) {
			n /= 10;
			count ++;
		}
		return count;
	}
	
	public static int numTrailingZeros(long n) {
		int count = 0;
		while (n % 10 == 0) {
			n /= 10;
			count ++;
		}
		return count;
	}
	
	public static Integer[] continuedFraction(int sqrt) {
		if (square(sqrt))
			return new Integer[0];
		int up = upperBound(sqrt);
		return getFrac(sqrt, up, -up, 1);
	}
	
	private static int upperBound(int sqrt) {
		int x = -1;
		while ((++x + 1) * (x + 1) <= sqrt);
		return x;
	}
	
	private static Integer[] getFrac(int sqrt, int sqrtBound, int n, int d) {
		d = (sqrt - n * n) / d;
		n = -n;
		int ret = 0;
		while (n - d >= -sqrtBound) {
			n -= d;
			ret ++;
		}
		if (n == -sqrtBound && d == 1)
			return new Integer[] {ret};
		return AO.append(ret, getFrac(sqrt, sqrtBound, n, d));
	}
	
}
