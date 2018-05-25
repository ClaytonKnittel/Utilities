package algorithms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import methods.P;
import structures.Graph;
import structures.Heap;

public class Dijkstra<T> {
	
	private Graph<T> graph;
	private HashMap<Graph<T>.GraphEdge, Float> weights;
	
	public static void main(String args[]) {
		LinkedList<Integer> i = new LinkedList<>();
		i.add(1);
		i.add(2);
		i.add(3);
		i.add(4);
		i.add(5);
		i.add(6);
		i.add(7);
		i.add(8);
		i.add(9);
		i.add(10);
		Dijkstra<Integer> d = new Dijkstra<>(i);
		d.addEdge(1, 9, 18);
		d.addEdge(1, 4, 12);
		d.addEdge(1, 6, 7);
		d.addEdge(9, 2, 9);
		d.addEdge(2, 4, 8);
		d.addEdge(6, 5, 8);
		d.addEdge(4, 5, 3);
		d.addEdge(4, 3, 6);
		d.addEdge(5, 10, 9);
		d.addEdge(3, 7, 4);
		d.addEdge(3, 8, 10);
		P.pl(d.shortestPath(6, 8));
	}
	
	public Dijkstra(List<T> nodes) {
		graph = new Graph<T>(nodes);
		this.weights = new HashMap<>();
	}
	
	public void addEdge(T from, T to, float weight) {
		Graph<T>.GraphEdge edge = graph.addEdge(from, to);
		Graph<T>.GraphEdge edge2 = graph.addEdge(to, from);
		weights.put(edge, weight);
		weights.put(edge2, weight);
	}
	
	public LinkedList<T> shortestPath(T start, T finish) {
		Heap<Distance> heap = new Heap<>(graph.numEdges(), new Distance(null), (e1, e2) -> e1.compareTo(e2));
		HashMap<Graph<T>.GraphNode, Distance> map = new HashMap<>(graph.size());
		
		for (Graph<T>.GraphNode node : graph.nodes()) {
			Distance d = new Distance(node);
			if (node.val() == start)
				d.decrease(0);
			heap.add(d);
			map.put(node, d);
		}
		
		Distance end = map.get(graph.getNode(finish));
		
		while (!heap.isEmpty()) {
			Distance d = heap.extractMax();
			if (d == end)
				break;
			findAll(d, end, heap, map);
		}
		
		LinkedList<T> path = new LinkedList<>();
		
		if (end.discoverer == null && start != finish)
			throw new IllegalStateException("Path from " + start + " to " + finish + " does not exist.");
		
		do {
			path.addFirst(end.val.val());
			end = end.discoverer;
		} while (end != null);
		
		return path;
		
	}
	
	private void findAll(Distance node, Distance end, Heap<Distance> heap, Map<Graph<T>.GraphNode, Distance> dist) {
		for (Graph<T>.GraphEdge e : node.val.edges()) {
			Distance d = dist.get(e.to());
			if (d.decrease(node.dist + weights.get(e))) {
				d.discoverer = node;
				heap.decrease(d);
			}
		}
	}
	
	private class Distance implements Comparable<Distance> {
		Graph<T>.GraphNode val;
		Distance discoverer;
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
	
}
