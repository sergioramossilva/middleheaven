package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Set;

public class DefaultScope implements Scope {

	@Override
	public <T> T scope(Class<T> type, Set<Annotation> annotations,  Resolver<T> resolver) {
		return resolver.resolve(type, annotations);
	}

}
