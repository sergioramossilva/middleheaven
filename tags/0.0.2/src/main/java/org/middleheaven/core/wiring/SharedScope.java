package org.middleheaven.core.wiring;

import java.util.HashMap;
import java.util.Map;

public final class SharedScope implements ScopePool {

	
	private final Map<Key , Object> objects = new HashMap<Key , Object>();
	
	
	@Override
	public <T> T getInScope(WiringSpecification<T> spec, Resolver<T> resolver) {
		Key key = Key.keyFor(spec.getContract(),spec.getSpecifications());
		
		@SuppressWarnings("unchecked") T obj = (T)objects.get(key);
		if (obj==null){
			obj = resolver.resolve(spec);
			objects.put(key,obj);
		}
		return obj;
	}


	@Override
	public <T> void add(WiringSpecification<T> spec, T object) {
		Key key = Key.keyFor(spec.getContract(),spec.getSpecifications());
		@SuppressWarnings("unchecked") T obj = (T)objects.get(key);
		if (obj==null){
			objects.put(key,object);
		} else {
			throw new UnsupportedOperationException("Trying to alter shared object " + obj + " to " + object);
		}
	}


	@Override
	public void remove(Object object) {
		objects.values().remove(object);
	}


	@Override
	public void clear() {
		this.objects.clear();
	}




}
