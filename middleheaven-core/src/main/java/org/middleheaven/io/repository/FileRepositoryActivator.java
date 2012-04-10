/**
 * 
 */
package org.middleheaven.io.repository;

import java.util.Collection;

import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.io.repository.classpath.ClassPathRepositoryProvider;
import org.middleheaven.io.repository.machine.MachineFileSystemRepositoryProvider;

/**
 * 
 */
public class FileRepositoryActivator extends ServiceActivator {

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
		specs.add(new ServiceSpecification(FileRepositoryService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		
		MapFileRepositoryService service = new MapFileRepositoryService();
		
		service.registerProvider(new ClassPathRepositoryProvider(service));
		service.registerProvider(MachineFileSystemRepositoryProvider.getProvider());
		
		// TODO VFS
		
		serviceContext.register(FileRepositoryService.class, service);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(FileRepositoryService.class);
	}

}
