package org.middleheaven.core.wiring;


public class NewInstanceScope extends AbstractScopePool {
	
	public NewInstanceScope(){}

	@Override
	public  Object getInScope(ResolutionContext context, WiringQuery query, Resolver resolver) {
		return resolver.resolve(context, query);
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
