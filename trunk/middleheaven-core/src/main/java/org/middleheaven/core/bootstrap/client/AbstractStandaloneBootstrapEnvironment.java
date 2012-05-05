/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap.client;

import org.middleheaven.application.ApplicationModulesResolver;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.BootstrapEnvironment;
import org.middleheaven.core.bootstrap.FileContext;
import org.middleheaven.core.bootstrap.StandardContainerFileSystem;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.io.repository.machine.MachineFileSystemRepositoryProvider;
import org.middleheaven.io.repository.machine.MachineFiles;

/**
 *
 */
public abstract class AbstractStandaloneBootstrapEnvironment implements BootstrapEnvironment {

	
	private StandardContainerFileSystem fileSystem;

	/**
	 * 
	 * Constructor.
	 * @param rootFolder the root folder where the container information can be stored.
	 */
	public AbstractStandaloneBootstrapEnvironment() {

		this.fileSystem = new StandardContainerFileSystem(MachineFiles.getDefaultFolder());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationModulesResolver getApplicationModulesResolver() {
		throw new UnsupportedOperationException("Not implememented yet");
	}
	
	@Override
	public void configurate(BootstrapContext context) {
		//no-op
	}
	
    @Override
	public void start() {
    	//no-op
    }

	@Override
	public void stop() {
		//no-op
	}
    
	@Override
	public FileContext getFileContext() {
		return fileSystem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepositoryProvider getManagedFileRepositoryProvider() {
		return MachineFileSystemRepositoryProvider.getProvider();
	}






}
