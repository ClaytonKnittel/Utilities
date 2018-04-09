package numbers;

import arrays.Pair;

public class IntPair extends Pair<Integer, Integer> {
	
	public IntPair(Integer x, Integer y) {
		super(x, y);
	}
	
	public int sum() {
		return x() + y();
	}
	
	public int product() {
		return x() * y();
	}
}
