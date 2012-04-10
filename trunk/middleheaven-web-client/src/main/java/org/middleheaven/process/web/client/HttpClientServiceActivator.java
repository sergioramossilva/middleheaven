/**
 * 
 */
package org.middleheaven.process.web.client;

import java.util.Collection;

import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.process.web.client.apache.ApacheHttpClientService;

/**
 * 
 */
public class HttpClientServiceActivator extends ServiceActivator{
	
	private HttpClientService service;

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
		specs.add(new ServiceSpecification(HttpClientService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		ApacheHttpClientService apacheService = new ApacheHttpClientService();
		
		apacheService.start();
		
		
		serviceContext.register(HttpClientService.class, apacheService);
		this.service = apacheService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		service.close();
	}

}
