/*
 * Created on 2007/01/25
 *
 */
package org.middleheaven.application;

import java.util.Collection;

import org.middleheaven.application.service.ApplicationService;
import org.middleheaven.dependency.Dependency;
import org.middleheaven.dependency.InstallContext;
import org.middleheaven.service.ServiceDependency;
import org.middleheaven.service.ServiceInstall;
import org.middleheaven.service.ServiceProvider;

public class ApplicationServiceModulesInstall<S extends ApplicationService> extends ServiceInstall<S>{

    private ApplicationConfiguration configuration;
    public ApplicationServiceModulesInstall(ServiceProvider provider ,ApplicationConfiguration configuration) {
        super(provider);
        this.configuration = configuration;
        
        for (Dependency d : configuration.getDependencies()){
            if (d instanceof ServiceDependency){
                this.addDependency(d);
            }
        }
    }

    public void install(InstallContext context) {
        // TODO Auto-generated method stub
        
    }

    
    public Collection<Dependency> getDependencies() {
        return dependencies;
    }
    
    public String toString(){
        return this.getClass().getName();
    }

}
