package org.middleheaven.util.collections;

import java.util.HashMap;

public class HashBoundMap<K, V> extends AbstractBoundMap<K,V>{

	public HashBoundMap() {
		super(new HashMap<K,V>(), new HashMap<V,K>());
	}

	



}
