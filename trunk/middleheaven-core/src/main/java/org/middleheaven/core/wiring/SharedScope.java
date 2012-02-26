package org.middleheaven.core.wiring;

import java.util.HashMap;
import java.util.Map;

public final class SharedScope extends AbstractScopePool {

	
	private final Map<Key , Object> objects = new HashMap<Key , Object>();
	
	
	@Override
	public <T> T getInScope(WiringSpecification<T> spec, Resolver<T> resolver) {
		Key key = Key.keyFor(spec.getContract(),spec.getParams());
		
		@SuppressWarnings("unchecked") T obj = (T)objects.get(key);
		if (obj==null){
			obj = resolver.resolve(spec);
			objects.put(key,obj);
			this.fireObjectAdded(obj);
		}
		return obj;
	}


	@Override
	public <T> void add(WiringSpecification<T> spec, T object) {
		Key key = Key.keyFor(spec.getContract(),spec.getParams());
		@SuppressWarnings("unchecked") T obj = (T)objects.get(key);
		if (obj==null){
			objects.put(key,object);
			this.fireObjectAdded(obj);
		} else {
			throw new UnsupportedOperationException("Trying to alter shared object " + obj + " to " + object);
		}
	}


	@Override
	public void remove(Object object) {
		if (objects.values().remove(object)){
			this.fireObjectRemoved(object);
		};
	}


	@Override
	public void clear() {
		this.objects.clear();
	}




}
