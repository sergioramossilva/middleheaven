package org.middleheaven.util.collections;

import java.util.Map;

/**
 * 
 * @param <K> the type for the key
 * @param <V> the type for the value.
 */
public interface EnhancedMap<K, V> extends Map<K,V> , Enumerable<Map.Entry<K, V>>{
	
	public EnhancedMap<K,V> asUnmodified();
	
	public EnhancedMap<K,V> asSynchronized();
	
	/**
	 * Determines if the this map contains the same key and value pairs as the {@code other} map.
	 * @param other
	 * @return <code>true</code> 
	 */
	public boolean containsSame(Map<K,V> other);
}
