package org.middleheaven.core.wiring.service;

import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.AbstractScopePool;
import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.WiringSpecification;

/**
 * Scope for services.
 * Services can be added and removed from the system, so
 * the pool in inject a proxy that can handle the service lifecycle.
 * 
 */
public class ServiceScope extends AbstractScopePool {


	public ServiceScope(){}
	
	@Override
	public <T> T getInScope(WiringSpecification<T> spec, Resolver<T> resolver) {

		if (resolver == null){
			throw new IllegalArgumentException("Argument 'resolver' is required");
		}
		try{
			return  ServiceRegistry.getService(spec.getContract(), spec.getParams());

		} catch (ServiceNotAvailableException e){
			T object = resolver.resolve(spec);
			
			if(object ==null){
				return null;
			}
			
			ServiceRegistry.register(spec.getContract(),object  , spec.getParams());
			this.fireObjectAdded(object);
			
			return ServiceRegistry.getService(spec.getContract(), spec.getParams());
			
		}

	}

	@Override
	public <T> void add(WiringSpecification<T> spec, T object) {
		ServiceRegistry.register(spec.getContract(), object,spec.getParams());
		this.fireObjectAdded(object);
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
