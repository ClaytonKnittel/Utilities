package structures;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InteractionSet<E, I extends Reversible<I>> {
	
	private Node[] nodes;
	private HashMap<E, Node> elements;
	private LinkedList<Connection<Node, I>> allConnections;
	
	@SuppressWarnings("unchecked")
	public InteractionSet(E[] elements) {
		this.elements = new HashMap<>();
		this.allConnections = new LinkedList<>();
		if (elements.length == 0) {
			nodes = null;
			return;
		}
		nodes = (Node[]) Array.newInstance(new Node(elements[0]).getClass(), elements.length);
		int i = 0;
		for (E e : elements)
			this.nodes[i++] = new Node(e);
		for (Node n : nodes)
			this.elements.put(n.val, n);
	}
	
	@SuppressWarnings("unchecked")
	public InteractionSet(List<E> elements) {
		this.elements = new HashMap<>();
		this.allConnections = new LinkedList<>();
		if (elements.size() == 0) {
			nodes = null;
			return;
		}
		nodes = (Node[]) Array.newInstance(new Node(elements.get(0)).getClass(), elements.size());
		int i = 0;
		for (E e : elements)
			this.nodes[i++] = new Node(e);
		for (Node n : nodes)
			this.elements.put(n.val, n);
	}
	
	public boolean empty() {
		return allConnections.isEmpty();
	}
	
	public int size() {
		return allConnections.size();
	}
	
	public void clear() {
		allConnections.clear();
		for (Node n : nodes)
			n.clear();
	}
	
	/**
	 * 
	 * @param connect e1 and e2 with connection
	 * @param e1
	 * @param e2
	 */
	public void add(I connection, E e1, E e2) {
		elements.get(e1).add(connection, e2);
	}
	
	/**
	 * Computes all coupled groups as a list of connections.
	 * <p>
	 * A coupled group is a group of connections who all share at least
	 * with at least one other connection in the group and are
	 * connected, meaning it is not possible to separate the group
	 * into two separate coupled groups.
	 * <p>
	 * Ex: take the group 1, 2, 3, 4, 5, 6, and the connections
	 * c(1, 2), c(2, 3), c(5, 6).
	 * 
	 * The coupled groups would be [c(1, 2), c(2, 3)], [c(5, 6)]
	 * 
	 * @return a list of the coupled groups of this InteractionSet
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<Connection<E, I>[]> groups(ConnectionRule<E> r) {
		
		LinkedList<Connection<E, I>[]> groupList = new LinkedList<>();
		
		for (Node n : elements.values()) {
			if (n.visited || !r.propagatesForce(n.val))
				continue;
			n.visit();
			
			LinkedList<Connection<E, I>> list = getAll(n, new LinkedList<>(), r);
			
			if (list.size() == 0)
				continue;
			
			Connection<E, I>[] l = (Connection<E, I>[]) Array.newInstance(list.get(0).getClass(), list.size());
			l = list.toArray(l);
			groupList.add(l);
		}
		
		for (Node n : elements.values())
			n.reset();
		for (Connection<Node, I> e : allConnections)
			e.reset();
		
		return groupList;
	}
	
	public LinkedList<Connection<E, I>[]> groups() {
		return groups(b -> true);
	}
	
	/**
	 * Describes which elements in the InteractionSet should be allowed to be
	 * connectors in groups and which shouldn't. If, for example, even numbers
	 * are force propagators, but odd numbers aren't. then in the set:
	 * <br>
	 * 1, 2, 3, 4, 5
	 * <br>
	 * with connections
	 * <br>
	 * 1->2, 2->3, 3->4, 4->5
	 * <br>
	 * the groups would be:
	 * {(1, 2, 3), (3, 4, 5)}
	 * 
	 * @author claytonknittel
	 *
	 * @param <E>
	 */
	public static interface ConnectionRule<E> {
		boolean propagatesForce(E e);
	}
	
	/**
	 * 
	 * @param root
	 * @param ret an empty list
	 * @return a list of all connections stemming from the root node
	 */
	private LinkedList<Connection<E, I>> getAll(Node root, LinkedList<Connection<E, I>> ret, ConnectionRule<E> r) {
		for (Connection<Node, I> c : root.connections) {
			Node to = c.to(root);
			if (c.passed)
				continue;
			c.pass();
			
			ret.add(new Connection<>(c.connector, c.from.val, c.to.val));
			if (r.propagatesForce(to.val))
				getAll(to, ret, r);
		}
		return ret;
	}
	
	
	private class Node {
		E val;
		LinkedList<Connection<Node, I>> connections;
		
		// used in the depth-first search to ensure no nodes are visited twice
		private boolean visited;
		
		Node(E val) {
			this.val = val;
			connections = new LinkedList<>();
		}
		
		void add(I connection, E to) {
			connections.add(new Connection<>(connection, this, elements.get(to)));
			elements.get(to).add(connections.getLast());
			allConnections.add(connections.getLast());
		}
		
		void add(Connection<Node, I> c) {
			connections.add(c);
		}
		
		void visit() {
			visited = true;
		}
		
		void reset() {
			visited = false;
		}
		
		void clear() {
			connections.clear();
		}
		
		@Override
		public int hashCode() {
			return val.hashCode();
		}
	}
	
	public static class Connection<T, I extends Reversible<I>> {
		I connector;
		T from;
		T to;
		
		boolean passed;
		
		Connection(I connector, T from, T to) {
			this.connector = connector;
			this.from = from;
			this.to = to;
			passed = false;
		}
		
		public T from() {
			return from;
		}
		
		public T to() {
			return to;
		}
		
		private void pass() {
			passed = true;
		}
		
		private void reset() {
			passed = false;
		}
		
		/**
		 * 
		 * @param which the element of the node of one end of this connection
		 * @return the element this connection connects to
		 */
		T to(T which) {
			if (from == which)
				return to;
			if (to == which)
				return from;
			return null;
		}
		
		public I connector() {
			return connector;
		}
		
		/**
		 * 
		 * @param which the element of the node of one end of this connection
		 * @return the connection from this element to the other
		 */
		public I connector(T which) {
			if (from == which)
				return connector;
			if (to == which)
				return connector.reverse();
			return null;
		}
		
		public String toString() {
			return "(" + from + " -> " + to + ")";
		}
	}
}
