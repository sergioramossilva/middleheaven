/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import org.middleheaven.application.ApplicationModulesResolver;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;


/**
 * The real container to complete the bootstrap cycle.
 */
public interface BootstrapEnvironment  {

    public String getName();
    
    public FileContext getFileContext();
    
    /**
     * Established and returns the {@link ManagedFileRepositoryProvider} to use with this container.
     *  
     * @return the {@link ManagedFileRepositoryProvider} to use with this container.
     */
    public ManagedFileRepositoryProvider getManagedFileRepositoryProvider();
    
    /**
     * 
     * @param context the bootstrat context.
     */
    public void configurate(BootstrapContext context);
    
    public void start();
    public void stop();

	/**
	 * @return
	 */
	public ApplicationModulesResolver getApplicationModulesResolver();
    

}
