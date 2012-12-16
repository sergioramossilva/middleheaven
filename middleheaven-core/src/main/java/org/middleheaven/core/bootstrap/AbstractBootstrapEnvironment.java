/**
 * 
 */
package org.middleheaven.core.bootstrap;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceBuilder;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceSpecification;
import org.omg.IOP.TransactionService;


/**
 * 
 */
public abstract class AbstractBootstrapEnvironment implements BootstrapEnvironment{

	private Map<String, Service> services = new HashMap<String, Service>();
	
	
	public AbstractBootstrapEnvironment(){}
	

	protected void addService(Service service){
		services.put(service.getServiceSpecification().getServiceContractType().getName(), service);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Service provideService(ServiceSpecification spec) {
		
		Service service = services.get(spec.getServiceContractType().getName());
		
		if (service != null){
			return service;
		}
		
		for (Service s : services.values()){
			if (s.getServiceSpecification().matches(spec)){
				return s;
			}
		}
		
		throw new ServiceNotAvailableException(spec.getServiceContractType().getName());
	}
}
