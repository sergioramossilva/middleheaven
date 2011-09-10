/**
 * 
 */
package org.middleheaven.io.repository;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.io.repository.classpath.ClassPathRepositoryProvider;

/**
 * 
 */
public class FileRepositoryActivator extends Activator {

	MapFileRepositoryService service = new MapFileRepositoryService();
	
	@Publish
	public FileRepositoryService getFileRepositoryService(){
		return service;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ActivationContext context) {
		service.registerProvider(new ClassPathRepositoryProvider(service));
		service.registerProvider(MachineFileSystemRepositoryProvider.getProvider());
		
		// TODO VFS
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ActivationContext context) {
		// no-op
	}

}
