package org.middleheaven.core.wiring;


public interface ScopePool {

	
	public <T> T scope (WiringSpecification<T> query, Resolver<T> resolver);
}
