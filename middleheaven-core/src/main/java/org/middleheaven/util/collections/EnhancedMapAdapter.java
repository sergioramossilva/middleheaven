package org.middleheaven.util.collections;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.NegationClassifier;

public class EnhancedMapAdapter<K,V> extends AbstractEnumerableAdapter<Map.Entry<K, V>> implements EnhancedMap<K, V> {

	
	private Map<K, V> original;

	protected EnhancedMapAdapter(Map<K,V> original){
		this.original = original;
	}
	
	
	@Override
	public void clear() {
		this.original.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return original.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return original.containsValue(value);
	}

	@Override
	public EnhancedSet<Map.Entry<K,V>> entrySet() {
		return CollectionUtils.enhance(original.entrySet());
	}

	@Override
	public V get(Object key) {
		return original.get(key);
	}

	@Override
	public boolean isEmpty() {
		return original.isEmpty();
	}

	@Override
	public EnhancedSet<K> keySet() {
		return CollectionUtils.enhance(original.keySet());
	}

	@Override
	public EnhancedCollection<V> values() {
		return CollectionUtils.enhance(original.values());
	}
	
	@Override
	public V put(K key, V value) {
		return original.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		original.putAll(m);
	}

	@Override
	public V remove(Object key) {
		return original.remove(key);
	}

	@Override
	public int size() {
		return original.size();
	}

	@Override
	public Iterator<java.util.Map.Entry<K, V>> iterator() {
		return original.entrySet().iterator();
	}

	@Override
	public boolean containsSame(Map<K, V> other) {
		return CollectionUtils.equalContents(this, other);
	}

	@Override
	public EnhancedMap<K, V> asSynchronized() {
		return new EnhancedMapAdapter<K, V>(Collections.synchronizedMap(this.original));
	}

	@Override
	public EnhancedMap<K, V> asUnmodified() {
		return new EnhancedMapAdapter<K, V>(Collections.unmodifiableMap(this.original));
	}


}
