package structures;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Graph<T> {
	
	private HashMap<T, GraphNode> nodes;
	private LinkedList<GraphEdge> edges;
	
	public Graph(T[] nodes) {
		this.nodes = new HashMap<>();
		for (T node : nodes)
			this.nodes.put(node, new GraphNode(node));
		edges = new LinkedList<>();
	}
	
	public Graph(List<T> nodes) {
		this.nodes = new HashMap<>();
		for (T node : nodes)
			this.nodes.put(node, new GraphNode(node));
		edges = new LinkedList<>();
	}
	
	public GraphEdge addEdge(T from, T to) {
		GraphNode f = nodes.get(from);
		GraphNode t = nodes.get(to);
		edges.add(new GraphEdge(f, t));
		f.addEdgeTo(edges.getLast());
		return edges.getLast();
	}
	
	public void remove(GraphNode node) {
		for (GraphEdge e : node.edgesTo)
			edges.remove(e);
		this.nodes.remove(node.val);
		node.clearConnections();
	}
	
	public Iterable<GraphNode> nodes() {
		return nodes.values();
	}
	
	public LinkedList<GraphEdge> edges() {
		return edges;
	}
	
	public int size() {
		return nodes.size();
	}
	
	public int numEdges() {
		return edges.size();
	}
	
	public GraphNode getNode(T el) {
		return nodes.get(el);
	}
	
	
	public String toString() {
		String ret = "";
		for (GraphEdge e : edges)
			ret += e.toString() + "\n";
		return ret;
	}
	
	
	public class GraphNode {
		
		private T val;
		private LinkedList<GraphEdge> edgesTo;
		
		public GraphNode(T val) {
			this.val = val;
			this.edgesTo = new LinkedList<>();
		}
		
		public T val() {
			return val;
		}
		
		public void addEdgeTo(GraphEdge e) {
			this.edgesTo.add(e);
		}
		
		public Iterable<GraphEdge> edges() {
			return edgesTo;
		}
		
		public int numConnections() {
			return edgesTo.size();
		}
		
		@SuppressWarnings("unlikely-arg-type")
		public GraphEdge removeConnection(GraphNode node) {
			int i = edgesTo.indexOf(node);
			GraphEdge e = edgesTo.remove(i);
			edges.remove(e);
			return e;
		}
		
		public void clearConnections() {
			this.edgesTo.clear();
		}
		
		public String toString() {
			return val + " " + edgesTo;
		}
		
		public String partString() {
			return val + "";
		}
		
		@SuppressWarnings("unchecked")
		public boolean equals(Object o) {
			if (o instanceof Graph.GraphNode)
				return o == this;
			if (!(o instanceof Graph.GraphEdge))
				return false;
			return ((GraphEdge) o).to == this;
		}
		
	}


	public class GraphEdge {
		
		private GraphNode from, to;
		
		public GraphEdge(GraphNode from, GraphNode to) {
			this.from = from;
			this.to = to;
		}
		
		public GraphNode from() {
			return from;
		}
		
		public GraphNode to() {
			return to;
		}
		
		@SuppressWarnings("unchecked")
		public boolean equals(Object o) {
			if (o instanceof Graph.GraphEdge)
				return this == o;
			if (!(o instanceof Graph.GraphNode))
				return false;
			GraphNode n = (GraphNode) o;
			return to == n;
		}
		
		public String toString() {
			return from.partString() + " -> " + to.partString();
		}
		
	}

	
}
