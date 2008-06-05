/*
 * Created on 2007/01/27
 *
 */
package org.middleheaven.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.middleheaven.application.service.ApplicationService;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.data.domain.service.DomainModelService;
import org.middleheaven.dependency.Dependency;
import org.middleheaven.dependency.InstallContext;
import org.middleheaven.domain.AnnotatedClassDomainLoader;
import org.middleheaven.domain.DomainModelLoader;
import org.middleheaven.domain.XMLDomainLoader;
import org.middleheaven.service.ServiceDependency;
import org.middleheaven.service.ServiceRegistry;

public class DomainModule extends ParameterizedModule {

    
    @Override
    public void install(InstallContext context) {
        // Determine Application ID
        ApplicationService service=null;
        ContextIdentifier id;
        try {
            service = ServiceRegistry.getService(ApplicationService.class);
            id =  new ContextIdentifier(service.getApplicationModel().getID());
        } finally {
            if (service!=null) service.dispose();
        }
        
        // determines with file, if any, will be used to load the entities. 
        String file = this.getParameter("domain.config.file");
        
        if (file==null || file.trim().length()==0){
            file =  service.getApplicationModel().getID() + "-domain.xml";
        }
        
        // determines loader and delegates loading
        DomainModelLoader loader;
        if (context.getExecutionEnvironment().getAppConfigRepository().exists(file)){
            // use XML to determine entities
            loader = new XMLDomainLoader(context.getExecutionEnvironment().getAppConfigRepository().retrive(file));
        } else {
            // use Annotations to determine entities.
            loader = new AnnotatedClassDomainLoader(context.getExecutionEnvironment().getAppClasspathRepository());
        }
        loader.load(id + "-model");
        
    }

    
    public Collection<Dependency> getDependencies(){
        List<Dependency> dependencies = new ArrayList<Dependency>();
        
        dependencies.add(new ServiceDependency<DomainModelService>(DomainModelService.class,true));
        
        return dependencies;
    }

}
