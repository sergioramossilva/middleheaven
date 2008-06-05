/*
 * Created on 2006/12/29
 *
 */
package org.middleheaven.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.middleheaven.data.domain.service.DomainModelService;
import org.middleheaven.dependency.Dependency;
import org.middleheaven.dependency.InstallContext;
import org.middleheaven.service.ServiceDependency;
import org.middleheaven.service.ServiceRegistry;

public class MainApplicationModule extends ParameterizedModule{

    @Override
    public void install(InstallContext context) {
        // TODO Lê domain e registra-o
        String file = this.getParameter("domain.file");
        System.out.println("domain.file=" + file);
        
        DomainModelService service = null;
        try {
            service = ServiceRegistry.getService(DomainModelService.class);
            
        } finally {
            if (service!=null)service.dispose();
        }
        // TODO Lê uienv e registra-o
    }

    
    public Collection<Dependency> getDependencies(){
        List<Dependency> dependencies = new ArrayList<Dependency>();
        
        dependencies.add(new ServiceDependency<DomainModelService>(DomainModelService.class,true));
        
        return dependencies;
    }
    
}
