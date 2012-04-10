package org.middleheaven.notification;


import java.util.Collection;

import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;

public class NotificationServiceActivator extends ServiceActivator {

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
		specs.add(new ServiceSpecification(NotificationService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		
		serviceContext.register(NotificationService.class, new MapNotificationService());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(NotificationService.class);
	}

	
	
	
}
