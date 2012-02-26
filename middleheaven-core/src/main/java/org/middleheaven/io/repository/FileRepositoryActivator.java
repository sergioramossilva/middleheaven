/**
 * 
 */
package org.middleheaven.io.repository;

import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.io.repository.classpath.ClassPathRepositoryProvider;
import org.middleheaven.io.repository.machine.MachineFileSystemRepositoryProvider;

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
	public void activate() {
		service.registerProvider(new ClassPathRepositoryProvider(service));
		service.registerProvider(MachineFileSystemRepositoryProvider.getProvider());
		
		// TODO VFS
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate() {
		// no-op
	}

}
