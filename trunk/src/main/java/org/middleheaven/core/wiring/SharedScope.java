package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SharedScope implements Scope {

	
	private final Map<Key , Object> OBJECTS = new HashMap<Key , Object>();
	
	@Override
	public <T> T scope(Class<T> type, Set<Annotation> annotations,Resolver<T> resolver) {
		
		T obj = (T)OBJECTS.get(Key.keyFor(type,annotations));
		if (obj==null){
			obj = resolver.resolve(type, annotations);
			OBJECTS.put(Key.keyFor(type,annotations),obj);
		}
		return obj;
	}

}
