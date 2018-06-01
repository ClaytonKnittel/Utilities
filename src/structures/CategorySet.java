package structures;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import structures.LList.Delete;

public class CategorySet<T> {
	
	private LList<Item>[] categories;
	private HashMap<T, Item> map;
	private Categorizer<T> c;
	
	@SuppressWarnings("unchecked")
	public CategorySet(int size, Categorizer<T> c) {
		categories = (LList<Item>[]) Array.newInstance(new LList<Item>().getClass(), size);
		clear();
		map = new HashMap<>();
		this.c = c;
	}
	
	public Set<T> itemList() {
		return map.keySet();
	}
	
	public void clear() {
		for (int i = 0; i < categories.length; i++)
			categories[i] = new LList<>();
	}
	
	public void add(T item) {
		Item i = new Item(item);
		add(i);
		map.put(item, i);
	}
	
	private void add(Item i) {
		for (Integer j : c.category(i.item)) {
			categories[j].add(i);
			i.add(j);
		}
	}
	
	public void remove(T item) {
		Item i = map.get(item);
		remove(i);
		map.remove(item);
	}
	
	private void remove(Item i) {
		for (Integer j : i.categories) {
			categories[j].remove(i);
		}
	}
	
	public void update(T item) {
		Item i = map.get(item);
		remove(i);
		i.categories.clear();
		add(i);
	}
	
	public void clearDeleted(Delete<T> d) {
		for (T key : map.keySet()) {
			if (d.delete(key))
				remove(map.get(key));
		}
	}
	
	public void actAll(PairAct<T> a) {
		PairAct<Item> c = (x, y) -> a.act(x.item, y.item);
		for (LList<Item> l : categories)
			l.actPairs(c);
	}
	
	private class Item {
		T item;
		List<Integer> categories;
		
		Item(T item) {
			this.item = item;
			categories = new LinkedList<>();
		}
		
		void add(int i) {
			categories.add(i);
		}
		
		public String toString() {
			return item + "";
		}
	}
	
	public String toString() {
		String ret = "";
		for (int i = 0; i < categories.length; i++)
			ret += i + ": \t" + categories[i] + "\n";
		return ret;
	}
	
	public static interface Categorizer<T> {
		Iterable<Integer> category(T t);
	}
	
}
