package structures;

import java.util.ArrayList;

public class Trie<T> {
	
	private Node root;
	private Parser<T> parser;
	private int n;
	
	/**
	 * @param numParts max number of parts in any given object (i.e. 10 for numbers and 26 for words)
	 */
	public Trie(int numParts, Parser<T> parser) {
		n = numParts;
		this.parser = parser;
		root = new Node(null, n, false);
	}
	
	public void add(T item) {
		Node current = root;
		int count = item.toString().length();
		for (T sub : parser.parse(item)) {
			count--;
			if (!current.has(sub))
				current.add(sub, count == 0);
			current = current.get(sub);
		}
	}
	
	/**
	 * 
	 * @param item
	 * @return false only if the item does not show up at all in the Trie (ignoring whether or not it is a complete item)
	 */
	public boolean isPart(T item) {
		Node current = root;
		for (T sub : parser.parse(item)) {
			if (!current.has(sub))
				return false;
			current = current.get(sub);
		}
		return true;
	}
	
	public boolean contains(T item) {
		Node current = root;
		for (T sub : parser.parse(item)) {
			if (!current.has(sub))
				return false;
			current = current.get(sub);
		}
		return current.complete;
	}
	
	private class Node {
		
		private T val;
		private ArrayList<Node> babies;
		
		// keeps track of which objets are the end of input objects (i.e. which letters are the last letter in their word)
		private final boolean complete;
		
		private Node(T val, int num, boolean last) {
			this.val = val;
			babies = new ArrayList<>(num);
			complete = last;
		}
		
		private boolean has(T val) {
			for (Node n : babies) {
				if (n.val.equals(val))
					return true;
			}
			return false;
		}
		
		private void add(T val, boolean last) {
			babies.add(new Node(val, n, last));
		}
		
		private Node get(T val) {
			for (Node n : babies) {
				if (n.val.equals(val))
					return n;
			}
			return null;
		}
		
		public String toString() {
			return val + "  " + babies;
		}
		
	}
	
	public String toString() {
		return root.toString();
	}
	
}
