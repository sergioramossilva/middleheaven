package org.middleheaven.core.wiring.service;

import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.AbstractScopePool;
import org.middleheaven.core.wiring.ResolutionContext;
import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.WiringQuery;

/**
 * Scope for services.
 * Services can be added and removed from the system, so
 * the pool in inject a proxy that can handle the service lifecycle.
 * 
 */
public class ServiceScope extends AbstractScopePool {


	public ServiceScope(){}
	
	@Override
	public Object getInScope(ResolutionContext context, WiringQuery query, Resolver resolver) {

		if (resolver == null){
			throw new IllegalArgumentException("Argument 'resolver' is required");
		}

		try{
			return  ServiceRegistry.getService(query.getContract(), query.getParams());

		} catch (ServiceNotAvailableException e){
			Object object = resolver.resolve(context, query);
			
			if(object ==null){
				return null;
			}
			
			ServiceRegistry.register(query.getContract(),object  , query.getParams());
			this.fireObjectAdded(object);
			
			return ServiceRegistry.getService(query.getContract(), query.getParams());
			
		}

	}

	@Override
	public void clear() {
		//no-op
	}

	@Override
	public void remove(Object object) {
		ServiceRegistry.unRegister(object);
		this.fireObjectRemoved(object);
	}

}
