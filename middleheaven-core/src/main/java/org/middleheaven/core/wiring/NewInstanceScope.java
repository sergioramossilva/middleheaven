package org.middleheaven.core.wiring;


public class NewInstanceScope extends AbstractScopePool {
	
	public NewInstanceScope(){}

	@Override
	public <T> T getInScope(WiringSpecification<T> query, Resolver<T> resolver) {
		return resolver.resolve(query);
	}

	@Override
	public <T> void add(WiringSpecification<T> spec, T object) {
		// no-op
	}

	@Override
	public void clear() {
		//no-op
	}

	@Override
	public void remove(Object object) {
		//no-op
	}

}
