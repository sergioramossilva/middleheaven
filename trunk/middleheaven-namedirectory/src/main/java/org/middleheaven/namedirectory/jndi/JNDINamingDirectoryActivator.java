package org.middleheaven.namedirectory.jndi;

import java.util.Collection;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.NamingDirectoryException;

/**
 * Activates a JNDI based {@link NamingDirectoryException} service.
 */
public class JNDINamingDirectoryActivator extends ServiceActivator {

	private JNDINameDirectoryService service;
	
	/**
	 * 
	 * Constructor.
	 */
	public JNDINamingDirectoryActivator (){}
	
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
		specs.add(new ServiceSpecification(NameDirectoryService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		service =  new JNDINameDirectoryService();
		
		serviceContext.register(NameDirectoryService.class, service);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		service = null;
	}

}
