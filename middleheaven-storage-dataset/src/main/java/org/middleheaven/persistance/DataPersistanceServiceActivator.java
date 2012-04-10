/**
 * 
 */
package org.middleheaven.persistance;

import java.util.Collection;

import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;

/**
 * 
 */
public class DataPersistanceServiceActivator extends ServiceActivator {

	DefaultDataService service;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		//no-dependencies
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(DataService.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		
		service = new DefaultDataService();
		
		serviceContext.register(DataService.class, service);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		service.close();
		service = null;
	}

}
