package tests;

import methods.P;

public class TimeTest {
	
	private Operation o;
	
	public TimeTest(Operation o) {
		this.o = o;
	}
	
	public void mathematicaForm(int start, int end, int stride, int numTrials) {
		double[] d = runTest(start, end, stride, numTrials);
		P.p("{");
		for (int i = 0; i < d.length; i++) {
			P.pl("{" + (start + stride * i) + "," + doubleStr(d[i]) + "}" + (i == d.length - 1 ? "}" : ","));
		}
	}
	
	public void printTests(int start, int end, int stride, int numTrials) {
		double[] d = runTest(start, end, stride, numTrials);
		for (int i = 0; i < d.length; i++) {
			P.pl((start + stride * i) + ", \t" + d[i]);
		}
	}
	
	public double[] runTest(int start, int end, int stride, int numTrials) {
		double[] results = new double[(end - start) / stride];
		long time;
		for (int i = 0; i < results.length; i++) {
			for (int j = 0; j < numTrials; j++) {
				time = System.nanoTime();
				o.operate(start + stride * i);
				results[i] += System.nanoTime() - time;
			}
			results[i] /= numTrials * 1000000000d;
		}
		return results;
	}
	
	public static interface Operation {
		void operate(int input);
	}
	
	private static String doubleStr(double d) {
		String s = d + "";
		String[] p = s.split("E");
		if (p.length > 1)
			s = p[0] + " 10^" + p[1];
		return s;
	}
	
}
