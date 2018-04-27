package structures;

import java.util.LinkedList;

public class Web<E> {
	
	private LinkedList<Node<E>> elements;
	
	public Web() {
		elements = new LinkedList<>();
	}
	
	public void add(E e) {
		elements.addLast(new Node<E>(e, elements));
	}
	
	public void act(Act<E> a) {
		for (Node<E> node : elements)
			a.act(node.val);
	}
	
	public void actPairs(PairAct<E> a) {
		for (Node<E> node : elements) {
			for (E e : node.elementsToRight) {
				a.act(node.val, e);
			}
		}
	}
	
	public void remove(E e) {
		for (Node<E> d : elements) {
			if (d.val == e)
				elements.remove(d);
			if (!d.remove(e))
				break;
		}
	}
	
	private static class Node<E> {
		LinkedList<E> elementsToRight;
		E val;
		
		Node(E val, Iterable<Node<E>> nodesTo) {
			elementsToRight = new LinkedList<>();
			this.val = val;
			for (Node<E> e : nodesTo)
				e.connect(this);
		}
		
		void connect(Node<E> e) {
			elementsToRight.addLast(e.val);
		}
		
		boolean remove(E e) {
			return elementsToRight.remove(e);
		}
		
		boolean contains(E e) {
			return elementsToRight.contains(e);
		}
	}
	
	public String toString() {
		String s = "[";
		for (Node<E> e : elements)
			s += e.val + ", ";
		s = s.substring(0, s.length() - 2);
		return s + "]";
	}
	
	public String testWeb() {
		String s = "";
		for (Node<E> n : elements) {
			for (Node<E> n2 : elements) {
				if (n == n2)
					s += "x ";
				else if (n2.contains(n.val))
					s += ". ";
				else
					s += "  ";
			}
			s += "\n";
		}
		return s;
	}
	
	public String getAll() {
		String s = "";
		for (Node<E> n : elements) {
			s += n.elementsToRight + "\n";
		}
		return s;
	}
	
}
