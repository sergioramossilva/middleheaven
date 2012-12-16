/**
 * 
 */
package org.middleheaven.core.services;

import java.util.Collection;

/**
 * 
 */
class ImplementationActivator extends ServiceActivator {

	private Object implementation;
	private ServiceSpecification serviceSpecification;

	/**
	 * Constructor.
	 * @param implementation
	 * @param serviceSpecification 
	 */
	public ImplementationActivator(Object implementation, ServiceSpecification serviceSpecification) {
		this.implementation = implementation;
		this.serviceSpecification = serviceSpecification;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		//none
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(serviceSpecification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		
		final Class<Object> serviceContractType = (Class<Object>) serviceSpecification.getServiceContractType();
		serviceContext.register(serviceContractType, implementation, serviceSpecification.getParameters());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(serviceSpecification.getServiceContractType());
	}

}
