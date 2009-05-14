package org.middleheaven.util.collections;

import java.util.Map;

/**
 * Map that allow only one unique value per key, permitting a reverse mapping of values to keys
 * 
 *
 * @param <K>
 * @param <V>
 */
public interface BoundMap<K,V> extends Map<K,V> {

	
	public Map<V,K> reversed();
}
