package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBoundMap<K,V> implements BoundMap<K,V> {

	
	private Map<K, V> direct;
	private Map<V, K> reversed;

	public AbstractBoundMap(Map<K,V> direct, Map<V,K> reversed){
		this.direct = direct;
		this.reversed = reversed;
	}
	
	@Override
	public Map<V, K> reversed() {
		// TODO
	}
	
	protected final Map<K,V> direct() {
		return direct;
	}
	
	@Override
	public void clear() {
		direct().clear();
		reversed().clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return direct().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return reversed().containsKey(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return direct().entrySet();
	}

	@Override
	public V get(Object key) {
		return direct().get(key);
	}

	@Override
	public boolean isEmpty() {
		return direct().isEmpty();
	}

	@Override
	public Set<K> keySet() {
		// TODO the modification of this set reflect on the modification of both
		// direct and reversed
		return direct().keySet();
	}

	@Override
	public V put(K key, V value) {
		// TODO verifiy uniqueness
		reversed().put(value, key);
		return direct().put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> arg0) {
		// TODO
		
	}

	@Override
	public V remove(Object key) {
		V value = direct().remove(key);
		reversed().remove(value);
		return value;
	}

	@Override
	public int size() {
		return direct().size();
	}

	@Override
	public Collection<V> values() {
		// TODO modification on this collection
		// propagate both direct and reverse
		return direct().values();
	}

}
