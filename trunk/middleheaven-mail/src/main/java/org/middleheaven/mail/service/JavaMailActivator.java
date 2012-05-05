package org.middleheaven.mail.service;

import java.util.Collection;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.mail.MailSendingService;
import org.middleheaven.namedirectory.NameDirectoryService;

public class JavaMailActivator extends ServiceActivator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(NameDirectoryService.class, true));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(MailSendingService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		
		NameDirectoryService nameService = serviceContext.getService(NameDirectoryService.class);
		

		MailSendingService service;
		
		if(nameService!=null){
			service = new NameDirectoryMailSessionSendingService(nameService);
		} else {
			service = new LocalMailSendingService();
		}
		
		serviceContext.register(MailSendingService.class, service);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(MailSendingService.class);
	}

}
