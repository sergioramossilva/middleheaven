/*
 * Created on 2006/11/11
 *
 */
package org.middleheaven.config;

import java.util.Collection;

import org.middleheaven.core.Container;
import org.middleheaven.dependency.Installable;


public interface ConfigurationContext {

    /**
     * 
     * @return an object that contains or points to the configuration data.
     * Examples are Readers , URL and XML elements 
     */
    public Object getConfigurationData();
    
    /**
     * 
     * @return the current ExecutionEnvironment
     */
    public Container getExecutionEnvironment();
    
    public void addInstallable(Installable installable);
    
    public Collection<Installable> getInstallables();
}
