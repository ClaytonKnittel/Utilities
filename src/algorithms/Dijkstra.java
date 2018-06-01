package algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import structures.Graph;
import structures.Heap;
import structures.ValGraph;

public class Dijkstra<T, V extends Number> {
	
	private ValGraph<T, V> graph;
	
	public Dijkstra(List<T> nodes) {
		graph = new ValGraph<>(nodes);
	}
	
	public Dijkstra(ValGraph<T, V> graph) {
		this.graph = graph;
	}
	
	public void addDirEdge(T from, T to, V weight) {
		graph.addDirEdge(from, to, weight);
	}
	
	public void addEdge(T from, T to, V weight) {
		graph.addEdge(from, to, weight);
	}
	
	public String toString() {
		return graph.toString();
	}
	
	@SuppressWarnings("unchecked")
	public void condense() {
		LinkedList<Graph<T>.GraphNode> toRemove = new LinkedList<>();
		
		for (Graph<T>.GraphNode node : graph.nodes()) {
			if (node.numConnections() == 2) {
				Iterator<Graph<T>.GraphEdge> i = node.edges().iterator();
				Graph<T>.GraphNode one = i.next().to();
				Graph<T>.GraphNode two = i.next().to();
				Graph<T>.GraphEdge e1 = one.removeConnection(node);
				Graph<T>.GraphEdge e2 = two.removeConnection(node);
				graph.addEdge(one.val(), two.val(), ((Addable<V>) graph.val(e1)).add(graph.val(e2)));
				toRemove.add(node);
			}
		}
		for (Graph<T>.GraphNode n : toRemove)
			graph.remove(n);
	}
	
	public LinkedList<V> shortestPath(T start, T finish) {
		Heap<Distance> heap = new Heap<>(graph.numEdges(), new Distance(null), (e1, e2) -> e1.compareTo(e2));
		HashMap<Graph<T>.GraphNode, Distance> map = new HashMap<>(graph.size());
		
		boolean first = false;
		for (Graph<T>.GraphNode node : graph.nodes()) {
			Distance d = new Distance(node);
			if (node.val().equals(start)) {
				d.decrease(0);
				first = true;
			}
			heap.add(d);
			map.put(node, d);
		}
		
		if (!first)
			throw new IllegalStateException("No such node " + start);
		
		Distance end = map.get(graph.getNode(finish));
		if (end == null)
			throw new IllegalStateException("No such node " + finish);
		
		while (!heap.isEmpty()) {
			Distance d = heap.extractMax();
			if (d.equals(end))
				break;
			findAll(d, end, heap, map);
		}
		
		LinkedList<V> path = new LinkedList<>();
		
		if (end.discoverer == null && start != finish)
			throw new IllegalStateException("Path from " + start + " to " + finish + " does not exist.");
		
		while (true) {
			if (end.discoverer == null)
				break;
			path.addFirst(graph.val(end.discoverer));
			end = map.get(end.discoverer.from());
		}
		
		return path;
		
	}
	
	private void findAll(Distance node, Distance end, Heap<Distance> heap, Map<Graph<T>.GraphNode, Distance> dist) {
		if (node.dist == -1)
			return;
		for (Graph<T>.GraphEdge e : node.val.edges()) {
			Distance d = dist.get(e.to());
			if (d.decrease(node.dist + graph.val(e).floatValue())) {
				d.discoverer = e;
				heap.decrease(d);
			}
		}
	}
	
	private class Distance implements Comparable<Distance> {
		Graph<T>.GraphNode val;
		Graph<T>.GraphEdge discoverer;
		float dist;
		
		Distance(Graph<T>.GraphNode val) {
			this.val = val;
			dist = -1;
		}
		
		boolean decrease(float dist) {
			if (this.dist > -1 && this.dist < dist)
				return false;
			this.dist = dist;
			return true;
		}
		
		public int compareTo(Distance d) {
			if (dist == d.dist)
				return 0;
			if (dist == -1)
				return -1;
			if (d.dist == -1)
				return 1;
			if (dist > d.dist)
				return -1;
			return 1;
		}
		
		public String toString() {
			return "" + dist;
		}
	}
	
	public static interface Addable<T> {
		T add(T o);
	}
	
}
