/*
 * Created on 2007/01/01
 *
 */
package org.middleheaven.config;

import java.util.Collection;

import org.middleheaven.core.Container;
import org.middleheaven.dependency.Installable;

public abstract class AbstractConfigurationContext implements ConfigurationContext {

    private Container env;
    private Collection<Installable> installables;
    
    public AbstractConfigurationContext(Container env , Collection<Installable> installables){
        this.env = env;
        this.installables = installables;
    }

    public Container getExecutionEnvironment() {
        return env;
    }
    
    public Collection<Installable> getInstallables() {
        return installables;
    }

    
    public void setExecutionEnvironment(Container env){
        this.env = env;
    }
    
    public void addInstallable(Installable installable){
        installables.add(installable);
    }
}
