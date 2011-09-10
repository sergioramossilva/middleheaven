package org.middleheaven.util.collections;

import java.util.Map;

public interface EnhancedMap<K, V> extends Map<K,V> , Enumerable<Map.Entry<K, V>>{
	
	public EnhancedMap<K,V> asUnmodified();
	
	public EnhancedMap<K,V> asSynchronized();
	
	public boolean containsSame(Map<K,V> other);
}
