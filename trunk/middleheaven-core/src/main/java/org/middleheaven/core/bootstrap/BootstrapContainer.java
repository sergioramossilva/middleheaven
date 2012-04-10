/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import org.middleheaven.io.repository.ManagedFileRepositoryProvider;


/**
 * The real container to complete the bootstrap cycle.
 */
public interface  BootstrapContainer  {

    public String getContainerName();
    
    public ContainerFileSystem getFileSystem();
    
    /**
     * Established and returns the {@link ManagedFileRepositoryProvider} to use with this container.
     *  
     * @return the {@link ManagedFileRepositoryProvider} to use with this container.
     */
    public ManagedFileRepositoryProvider getManagedFileRepositoryProvider();
    
    public void configurate(ExecutionContext context);
    
    public void start();
    public void stop();
    

}
