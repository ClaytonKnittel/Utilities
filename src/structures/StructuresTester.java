package structures;

import java.util.LinkedList;

import structures.InteractionSet.Connection;

public class StructuresTester {
	
	public static void main(String args[]) {
		InteractionSet<Integer, Connector> set = new InteractionSet<>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
		
		set.add(new Connector(3), 2, 3);
		set.add(new Connector(4), 3, 4);
		set.add(new Connector(5), 1, 5);
		set.add(new Connector(6), 2, 3);
		set.add(new Connector(7), 2, 3);
		set.add(new Connector(8), 10, 5);
		
		LinkedList<Connection<Integer, Connector>[]> list = set.groups();
		
		for (Connection<Integer, Connector>[] c : list) {
			for (Connection<Integer, Connector> d : c) {
				System.out.print(d.connector.len + " ");
			}
			System.out.println();
		}
	}
	
	
	private static class Connector implements Reversible<Connector> {
		int len;
		
		Connector(int len) {
			this.len = len;
		}
		
		public Connector reverse() {
			return this;
		}
	}
	
}
