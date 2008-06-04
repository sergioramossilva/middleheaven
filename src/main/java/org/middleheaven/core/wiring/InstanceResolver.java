package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Set;

public class InstanceResolver<T> implements Resolver<T> {

	T object;
	InstanceResolver(T object){
		this.object = object;
	}
	
	@Override
	public T resolve(Class<T> type, Set<Annotation> annotations) {
		return object;
	}


}
