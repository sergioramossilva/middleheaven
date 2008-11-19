package org.middleheaven.core.wiring;


public class DefaultScope implements ScopePool {

	@Override
	public <T> T scope(WiringSpecification<T> query, Resolver<T> resolver) {
		return resolver.resolve(query);
	}

}
