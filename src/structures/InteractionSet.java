package structures;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InteractionSet<E, I extends Reversible<I>> {
	
	private HashMap<E, Node> elements;
	private LinkedList<Connection<Node, I>> allConnections;
	
	public InteractionSet(E[] elements) {
		this.elements = new HashMap<>();
		for (E e : elements)
			this.elements.put(e, new Node(e));
		this.allConnections = new LinkedList<>();
	}
	
	public InteractionSet(List<E> elements) {
		this.elements = new HashMap<>();
		for (E e : elements)
			this.elements.put(e, new Node(e));
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
	public LinkedList<Connection<E, I>[]> groups() {
		
		LinkedList<Connection<E, I>[]> groupList = new LinkedList<>();
		Connection<E, I>[] c;
		
		for (Node n : elements.values()) {
			if (n.visited)
				continue;
			n.visit();
			
			List<Connection<E, I>> list = getAll(n, new LinkedList<>());
			c = (Connection[]) Array.newInstance(Connection.class, list.size());
			list.toArray(c);
			groupList.add(c);
		}
		
		for (Node n : elements.values())
			n.reset();
		for (Connection<Node, I> e : allConnections)
			e.reset();
		
		return groupList;
	}
	
	/**
	 * 
	 * @param root
	 * @param ret an empty list
	 * @return a list of all connections stemming from the root node
	 */
	private LinkedList<Connection<E, I>> getAll(Node root, LinkedList<Connection<E, I>> ret) {
		for (Connection<Node, I> c : root.connections) {
			Node to = c.to(root);
			if (c.passed)
				continue;
			c.pass();
			ret.add(new Connection<>(c.connector, c.from.val, c.to.val));
			getAll(to, ret);
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
			return from;
		}
		
		/**
		 * 
		 * @param which the element of the node of one end of this connection
		 * @return the connection from this element to the other
		 */
		public I connector(T which) {
			if (from == which)
				return connector;
			return connector.reverse();
		}
	}
}
