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
	
	
	
	public class GraphNode {
		
		private T val;
		private LinkedList<GraphEdge> edges;
		
		public GraphNode(T val) {
			this.val = val;
			this.edges = new LinkedList<>();
		}
		
		public T val() {
			return val;
		}
		
		public void addEdgeTo(GraphEdge e) {
			this.edges.add(e);
		}
		
		public Iterable<GraphEdge> edges() {
			return edges;
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
		
	}

	
}
