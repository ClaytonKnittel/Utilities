package structures;

import java.util.HashMap;
import java.util.List;

public class ValGraph<T, V> extends Graph<T> {
	
	private HashMap<GraphEdge, V> map;

	public ValGraph(T[] nodes) {
		super(nodes);
		map = new HashMap<>();
	}

	public ValGraph(List<T> nodes) {
		super(nodes);
		map = new HashMap<>();
	}
	
	public void remove(GraphNode n) {
		super.remove(n);
	}
	
	@Override
	public GraphEdge addEdge(T to, T from) {
		throw new java.lang.UnsupportedOperationException("You must specify the value attributed to each edge in a ValGraph");
	}
	
	public GraphEdge addDirEdge(T to, T from, V val) {
		GraphEdge edge = super.addEdge(from, to);
		map.put(edge, val);
		return edge;
	}
	
	@SuppressWarnings("unchecked")
	public void addEdge(T to, T from, V val) {
		map.put(super.addEdge(from, to), val);
		if (Reversible.class.isAssignableFrom(val.getClass()))
			map.put(super.addEdge(to, from), ((Reversible<V>) val).reverse());
		else
			map.put(super.addEdge(to, from), val);
	}
	
	public V val(GraphEdge e) {
		return map.get(e);
	}
	
}
