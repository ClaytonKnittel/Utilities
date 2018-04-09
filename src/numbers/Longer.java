package numbers;

import arrays.AO;

public class Longer extends Number implements Comparable<Longer> {
	
	private static final long serialVersionUID = 9050937192562153909L;

	private boolean[] bitData;
	
	public static final Longer zero = new Longer(0);
	public static final Longer one = new Longer(1);
	public static final Longer nine = new Longer(9);
	public static final Longer ten = new Longer(10);
	
	public Longer(int i) {
		this.bitData = N.bitData(i);
	}
	
	public Longer(long l) {
		this.bitData = N.bitData(l);
	}
	
	public Longer(int l, int bits) {
		this.bitData = N.bitData(l, bits - 1);
	}
	
	public Longer(long l, int bits) {
		this.bitData = N.bitData(l, bits - 1);
	}
	
	public Longer(Longer l) {
		this.bitData = new boolean[l.numBits()];
		for (int x = 0; x < bitData.length; x++)
			bitData[x] = l.bitData[x];
	}
	
	public Longer(String s) {
		this(partitionString(s));
		if (s.charAt(0) == '-')
			neg();
	}
	
	private static int[] partitionString(String s) {
		int[] part;
		int x = 0;
		int neg = 0;
		if (s.charAt(x) == '-')
			neg = 1;
		part = new int[s.length() - neg];
		while (x < part.length) {
			part[x] = s.charAt(x + neg) - 48;
			x ++;
		}
		return part;
	}
	
	public Longer(int[] i) {
		bitData = zero.bitData();
		for (int x : i) {
			bitData = add(mult(bitData, ten.bitData()), N.bitData(x));
		}
	}
	
	private Longer(boolean[] data) {
		this.bitData = data;
	}
	
	
	@Override
	public int intValue() {
		int mult = 1, ret = 0, x = numBits();
		while (--x > 0) {
			ret += (bitData[x] ? mult : 0);
			mult *= 2;
		}
		return ret;
	}
	
	@Override
	public long longValue() {
		long mult = 1, ret = 0;
		int x = numBits();
		while (--x > 0) {
			ret += (bitData[x] ? mult : 0);
			mult *= 2;
		}
		return ret;
	}
	
	@Override
	public float floatValue() {
		return (float) intValue();
	}
	
	@Override
	public double doubleValue() {
		return (double) longValue();
	}
	
	@Override
	public byte byteValue() {
		return (byte) intValue();
	}
	
	@Override
	public short shortValue() {
		return (short) intValue();
	}
	
	public boolean[] bitData() {
		return AO.clone(bitData);
	}
	
	public boolean sign() {
		return sign(bitData);
	}
	
	private static boolean sign(final boolean[] a) {
		return a[0];
	}
	
	public void neg() {
		bitData[0] = !bitData[0];
	}
	
	public static Longer neg(Longer l) {
		return new Longer(neg(l.bitData));
	}
	
	private static boolean[] neg(final boolean[] a) {
		boolean[] ret = AO.clone(a);
		ret[0] = !ret[0];
		return ret;
	}
	
	public void abs() {
		bitData[0] = false;
	}
	
	public static Longer abs(Longer l) {
		return new Longer(abs(l.bitData));
	}
	
	private static boolean[] abs(boolean[] a) {
		boolean[] ret = AO.clone(a);
		ret[0] = false;
		return ret;
	}
	
	public boolean even() {
		return even(this);
	}
	
	public static boolean even(Longer l) {
		return l.bitData[0];
	}
	
	
	
	
	public void lShift(int n) {
		int x;
		for (x = 1; x <= n; x++) {
			if (bitData[x])
				appendBits(n + 1 - x);
		}
		x = 0;
		while (x < bitData.length - n - 1)
			bitData[++x] = bitData[x + n];
		while (x < bitData.length - 1)
			bitData[++x] = false;
	}
	
	public void rShift(int n) {
		int x = bitData.length;
		while (x > n + 1)
			bitData[--x] = bitData[x - n];
		while (x > 1)
			bitData[--x] = false;
	}
	
	public static Longer lShift(Longer l, int n) {
		return new Longer(lShift(l.bitData, n));
	}
	
	private static boolean[] lShift(final boolean[] data, int n) {
		if (zero.equals(data) || n == 0)
			return data;
		boolean[] ret = new boolean[data.length + n];
		int x = 0;
		while (x < data.length - 1)
			ret[++x] = data[x];
		while (x < ret.length - 1)
			ret[++x] = false;
		ret[0] = data[0];
		return ret;
	}
	
	public static Longer rShift(Longer l, int n) {
		return new Longer(rShift(l.bitData, n));
	}
	
	private static boolean[] rShift(final boolean[] data, int n) {
		boolean[] ret = new boolean[data.length - n];
		int x = ret.length;
		while (x > 1)
			ret[--x] = data[x];
		ret[0] = data[0];
		return ret;
	}
	
	public void and(Longer l) {
		bitData = and(bitData, l.bitData);
	}
	
	public static Longer and(Longer a, Longer b) {
		return new Longer(and(a.bitData, b.bitData));
	}
	
	private static boolean[] and(boolean[] a, boolean[] b) {
		boolean[] ret = new boolean[Math.max(a.length, b.length)];
		boolean[] c = appendBits(a, ret.length - a.length);
		boolean[] d = appendBits(b, ret.length - b.length);
		for (int x = ret.length - 1; x >= 0; x--)
			ret[x] = c[x] && d[x];
		ret[0] = c[0] && d[0];
		return ret;
	}
	
	public void or(Longer l) {
		bitData = or(bitData, l.bitData);
	}
	
	public static Longer or(Longer a, Longer b) {
		return new Longer(or(a.bitData, b.bitData));
	}
	
	private static boolean[] or(final boolean[] a, final boolean[] b) {
		boolean[] ret = new boolean[Math.max(a.length, b.length)];
		boolean[] c = appendBits(a, ret.length - a.length);
		boolean[] d = appendBits(b, ret.length - b.length);
		for (int x = ret.length - 1; x >= 0; x--)
			ret[x] = c[x] || d[x];
		return ret;
	}
	
	public void xor(Longer l) {
		bitData = xor(bitData, l.bitData);
	}
	
	public static Longer xor(Longer a, Longer b) {
		return new Longer(xor(a.bitData, b.bitData));
	}
	
	private static boolean[] xor(boolean[] a, boolean[] b) {
		boolean[] ret = new boolean[Math.max(a.length, b.length)];
		boolean[] c = appendBits(a, ret.length - a.length);
		boolean[] d = appendBits(b, ret.length - b.length);
		for (int x = ret.length - 1; x >= 0; x--)
			ret[x] = c[x] ^ d[x];
		return ret;
	}
	
	public void add(int i) {
		add(N.bitData(i));
	}
	
	public void add(long l) {
		add(N.bitData(l));
	}
	
	public void add(Longer l) {
		add(l.bitData);
	}
	
	private void add(boolean[] bits) {
		bitData = add(bitData, bits);
	}
	
	public static Longer add(Longer a, int b) {
		return new Longer(add(a.bitData, N.bitData(b)));
	}
	
	public static Longer add(Longer a, long b) {
		return new Longer(add(a.bitData, N.bitData(b)));
	}
	
	public static Longer add(Longer a, Longer b) {
		return new Longer(add(a.bitData, b.bitData));
	}
	
	private static boolean[] add(boolean[] a, boolean[] b) {
		boolean[] c = AO.clone(a);
		boolean[] d = AO.clone(b);
		if (c[0]) {
			c = add(neg(c), neg(d));
			return neg(c);
		}
		if (d[0])
			return sub(c, neg(d));
		
		boolean carry = false;
		boolean[] ret = new boolean[Math.max(c.length, d.length)];
		int da = ret.length - c.length, db = ret.length - d.length;
		c = appendBits(c, da);
		d = appendBits(d, db);
		int x;
		for (x = ret.length - 1; x > 0; x--) {
			ret[x] = c[x] ^ d[x] ^ carry;
			carry = c[x] && d[x] || ((c[x] || d[x]) && carry);
		}
		if (carry) {
			boolean[] n = new boolean[ret.length + 1];
			n[0] = ret[0];
			for (x = 1; x < ret.length; x++)
				n[x + 1] = ret[x];
			n[1] = true;
			return n;
		}
		return ret;
	}
	
	
	public void sub(int i) {
		sub(N.bitData(i));
	}
	
	public void sub(long l) {
		sub(N.bitData(l));
	}
	
	public void sub(Longer l) {
		sub(l.bitData);
	}
	
	private void sub(boolean[] bits) {
		bitData = sub(bitData, bits);
	}
	
	public static Longer sub(Longer a, int b) {
		return new Longer(sub(a.bitData, N.bitData(b)));
	}
	
	public static Longer sub(Longer a, long b) {
		return new Longer(sub(a.bitData, N.bitData(b)));
	}
	
	public static Longer sub(int a, Longer b) {
		return new Longer(sub(N.bitData(a), b.bitData));
	}
	
	public static Longer sub(long a, Longer b) {
		return new Longer(sub(N.bitData(a), b.bitData));
	}
	
	public static Longer sub(Longer a, Longer b) {
		return new Longer(sub(a.bitData, b.bitData));
	}
	
	private static boolean[] sub(boolean[] a, boolean[] b) {
		boolean[] x = AO.clone(a);
		boolean[] y = AO.clone(b);
		if (x[0]) {
			x = sub(neg(x), neg(y));
			return neg(x);
		}
		if (y[0])
			return add(x, neg(y));
		
		boolean[] ret = new boolean[Math.max(x.length, y.length)];
		int da = ret.length - x.length, db = ret.length - y.length;
		x = appendBits(x, da);
		y = appendBits(y, db);
		int z;
		for (z = ret.length - 1; z >= 0; z--) {
			ret[z] = x[z] ^ y[z];
			if (!x[z] && y[z]) {
				for (int w = z - 1; w >= 0; w--) {
					x[w] = !x[w];
					if (!x[w])
						break;
				}
			}
		}
		if (ret[0]) {
			for (z = 0; z < ret.length; z++)
				ret[z] = !ret[z];
			ret = add(ret, one.bitData); // + 1
			ret[0] = true;
		}
		return ret;
	}
	
	public void mult(int i) {
		mult(N.bitData(i));
	}
	
	public void mult(long l) {
		mult(N.bitData(l));
	}
	
	public void mult(Longer l) {
		mult(l.bitData);
	}
	
	private void mult(boolean[] bits) {
		bitData = mult(bitData, bits);
	}
	
	public static Longer mult(Longer a, int b) {
		return new Longer(mult(a.bitData, N.bitData(b)));
	}
	
	public static Longer mult(Longer a, long b) {
		return new Longer(mult(a.bitData, N.bitData(b)));
	}
	
	public static Longer mult(int a, Longer b) {
		return new Longer(mult(N.bitData(a), b.bitData));
	}
	
	public static Longer mult(long a, Longer b) {
		return new Longer(mult(N.bitData(a), b.bitData));
	}
	
	public static Longer mult(Longer a, Longer b) {
		return new Longer(mult(a.bitData, b.bitData));
	}
	
	private static boolean[] mult(boolean[] a, boolean[] b) {
		boolean[] ret = zero.bitData();
		int pos = b.length;
		while (pos > 1) {
			if (b[--pos]) {
				ret = add(ret, lShift(a, b.length - 1 - pos));
			}
		}
		ret[0] = sign(a) ^ sign(b);
		return ret;
	}
	
	public void div(int i) {
		bitData = div(this.bitData, N.bitData(i));
	}
	
	public void div(long l) {
		bitData = div(this.bitData, N.bitData(l));
	}
	
	public void div(Longer l) {
		bitData = div(this.bitData, l.bitData);
	}
	
	public static Longer div(Longer a, int b) {
		return new Longer(div(a.bitData, N.bitData(b)));
	}
	
	public static Longer div(Longer a, long b) {
		return new Longer(div(a.bitData, N.bitData(b)));
	}
	
	public static Longer div(Longer a, Longer b) {
		return new Longer(div(a.bitData, b.bitData));
	}
	
	private static boolean[] div(boolean[] a, boolean[] b) {
		int shift = a.length - b.length;
		boolean[] x = AO.clone(a);
		boolean[] y = AO.clone(b);
		boolean[] q = zero.bitData();
		if (shift < 0)
			return q;
		y = lShift(y, shift);
		do {
			q = lShift(q, 1);
			if (greaterOrEqual(x, y)) {
				x = sub(x, y);
				q = add(q, one.bitData());
			}
			y = rShift(y, 1);
		} while (--shift >= 0);
		q[0] = sign(x) ^ sign(y);
		return q;
	}
	
	public void pow(int pow) {
		bitData = pow(bitData, N.bitData(pow));
	}
	
	public void pow(long pow) {
		bitData = pow(bitData, N.bitData(pow));
	}
	
	public void pow(Longer pow) {
		bitData = pow(bitData, pow.bitData);
	}
	
	public static Longer pow(Longer a, int pow) {
		return new Longer(pow(a.bitData, N.bitData(pow)));
	}
	
	public static Longer pow(Longer a, long pow) {
		return new Longer(pow(a.bitData, N.bitData(pow)));
	}
	
	public static Longer pow(Longer a, Longer pow) {
		return new Longer(pow(a.bitData, pow.bitData));
	}
	
	private static boolean[] pow(boolean[] a, boolean[] pow) {
		boolean[] x = one.bitData();
		boolean[] y = AO.clone(pow);
		while (greater(y, zero.bitData)) {
			x = mult(x, a);
			y = sub(y, one.bitData);
		}
		return x;
	}
	
	public void mod(int n) {
		bitData = mod(bitData, N.bitData(n));
	}
	
	public void mod(long l) {
		bitData = mod(bitData, N.bitData(l));
	}
	
	public void mod(Longer l) {
		bitData = mod(bitData, l.bitData);
	}
	
	public static Longer mod(Longer l, int n) {
		return new Longer(mod(l.bitData, N.bitData(n)));
	}
	
	public static Longer mod(Longer l, long n) {
		return new Longer(mod(l.bitData, N.bitData(n)));
	}
	
	public static Longer mod(Longer l, Longer n) {
		return new Longer(mod(l.bitData, n.bitData));
	}
	
	private static boolean[] mod(boolean[] a, boolean[] b) {
		return sub(a, mult(div(a, b), b));
	}
	
	
	
	
	
	private void appendBits(int numBits) {
		bitData = appendBits(bitData, numBits);
	}
	
	private static boolean[] appendBits(boolean[] b, int numBits) {
		if (numBits == 0)
			return AO.clone(b);
		boolean[] newBits = new boolean[b.length + numBits];
		for (int x = 0; x < b.length - 1; x++)
			newBits[x + numBits + 1] = b[x + 1];
		if (b.length > 0)
			newBits[0] = b[0];
		return newBits;
	}
	
	public int numBits() {
		return bitData.length;
	}
	
	@Override
	public int compareTo(Longer l) {
		return compareTo(bitData, l.bitData);
	}
	
	private int compareTo(boolean[] b) {
		return compareTo(bitData, b);
	}
	
	private static int compareTo(boolean[] a, boolean[] b) {
		int tx = 0, lx = 0;
		if (b.length < a.length) {
			for (tx = 0; tx < a.length - b.length; tx++) {
				if (a[tx])
					return 1;
			}
		} else if (a.length < b.length) {
			for (lx = 0; lx < b.length - a.length; lx++) {
				if (b[lx])
					return -1;
			}
		}
		while (tx < a.length) {
			if (a[tx] && !b[lx])
				return 1;
			if (b[lx] && !a[tx])
				return -1;
			tx ++;
			lx ++;
		}
		return 0;
	}
	
	public boolean greater(Longer l) {
		return compareTo(l) > 0;
	}
	
	protected static boolean greater(boolean[] a, boolean[] b) {
		return compareTo(a, b) > 0;
	}
	
	public boolean less(Longer l) {
		return compareTo(l) < 0;
	}
	
	protected static boolean less(boolean[] a, boolean[] b) {
		return compareTo(a, b) < 0;
	}
	
	public boolean greaterOrEqual(Longer l) {
		return compareTo(l) >= 0;
	}
	
	protected static boolean greaterOrEqual(boolean[] a, boolean[] b) {
		return compareTo(a, b) >= 0;
	}
	
	public boolean lessOrEqual(Longer l) {
		return compareTo(l) <= 0;
	}
	
	protected static boolean lessOrEqual(boolean[] a, boolean[] b) {
		return compareTo(a, b) <= 0;
	}
	
	public boolean greater(int l) {
		return compareTo(N.bitData(l)) > 0;
	}
	
	public boolean less(int l) {
		return compareTo(N.bitData(l)) < 0;
	}
	
	public boolean greaterOrEqual(int l) {
		return compareTo(N.bitData(l)) >= 0;
	}
	
	public boolean lessOrEqual(int l) {
		return compareTo(N.bitData(l)) <= 0;
	}
	
	public boolean greater(long l) {
		return compareTo(N.bitData(l)) > 0;
	}
	
	public boolean less(long l) {
		return compareTo(N.bitData(l)) < 0;
	}
	
	public boolean greaterOrEqual(long l) {
		return compareTo(N.bitData(l)) >= 0;
	}
	
	public boolean lessOrEqual(long l) {
		return compareTo(N.bitData(l)) <= 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof boolean[])
			return equals(bitData, (boolean[]) o);
		if (!(o instanceof Longer))
			return false;
		return this.compareTo((Longer) o) == 0;
	}
	
	protected static boolean equals(boolean[] a, boolean[] b) {
		return compareTo(a, b) == 0;
	}
	
	
    private final static char [] DigitTens = {
        '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
        '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
        '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
        '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
        '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
        '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
        '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
        '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
        '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
        '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
        } ;

    private final static char [] DigitOnes = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        } ;
    
    private final static char[] digits = {
            '0' , '1' , '2' , '3' , '4' , '5' ,
            '6' , '7' , '8' , '9' , 'a' , 'b' ,
            'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
            'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
            'o' , 'p' , 'q' , 'r' , 's' , 't' ,
            'u' , 'v' , 'w' , 'x' , 'y' , 'z'
        };
	
	@Override
	public String toString() {
		if (equals(Longer.zero))
			return "0";
		
		int charPos = size();
		char[] buf = new char[charPos];
		Longer i = abs(new Longer(this)), q, r;
        while (i.greater(nine)) {
            q = div(i, 100);
            r = sub(i, add(add(lShift(q, 6), lShift(q, 5)), lShift(q, 2)));
            i = q;
            buf [--charPos] = digitOnes(r);
            buf [--charPos] = digitTens(r);
        }
        while (i.greater(zero)) {
            q = div(i, 10);
            r = sub(i, add(lShift(q, 3), lShift(q, 1)));
            i = q;
            buf [--charPos] = digitOnes(r);
        }
        if (sign()) {
            buf [--charPos] = '-';
        }
        return new String(buf);
	}
	
	public String toString(int lenLine) {
		String buf = toString();
		for (int x = lenLine - 1; x < buf.length(); x += lenLine) {
			buf = buf.substring(0, x) + "\n" + buf.substring(x);
		}
		return buf;
	}
	
	private static char digitOnes(Longer pos) {
		return DigitOnes[pos.intValue()];
	}
	
	private static char digitTens(Longer pos) {
		return DigitTens[pos.intValue()];
	}
	
	@SuppressWarnings("unused")
	private static char digits(Longer pos) {
		return digits[pos.intValue()];
	}
	
	public int size() {
		Longer compare = new Longer(10);
		int size = 1;
		while (greaterOrEqual(compare)) {
			size ++;
			compare.mult(10);
		}
		return size + (sign() ? 1 : 0);
	}
	
	public String bitString() {
		return bitString(bitData);
	}
	
	private static String bitString(boolean[] b) {
		String ret = "";
		for (boolean a : b)
			ret += a ? 1 : 0;
		return ret;
	}
	
	public String longString() {
		return longString(bitData);
	}
	
	private static String longString(boolean[] b) {
		long r = 0, n = 1;
		for (int x = b.length - 1; x > 0; x--) {
			if (b[0])
				r -= (b[x] ? n : 0);
			else
				r += (b[x] ? n : 0);
			n *= 2;
		}
		return "" + r;
	}
	
}
