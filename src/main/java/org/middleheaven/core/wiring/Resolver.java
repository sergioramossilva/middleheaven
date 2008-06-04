package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface Resolver<T> {

	
	public T resolve(Class<T> type, Set<Annotation> annotations);
}
