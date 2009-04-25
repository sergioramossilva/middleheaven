package org.middleheaven.core.wiring;


public class DefaultScope implements ScopePool {

	@Override
	public <T> T getInScope(WiringSpecification<T> query, Resolver<T> resolver) {
		return resolver.resolve(query);
	}

	@Override
	public <T> void add(WiringSpecification<T> spec, T object) {
		// TODO implement ScopePool.add
		
	}

	@Override
	public void clear() {
		// TODO implement ScopePool.clear
		
	}

	@Override
	public void remove(Object object) {
		// TODO implement ScopePool.remove
		
	}

}
