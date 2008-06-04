package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface Scope {

	
	public <T> T scope (Class<T> type, Set<Annotation> annotations,  Resolver<T> resolver);
}
