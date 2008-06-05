/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.application;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.middleheaven.config.ConfigurationContext;
import org.middleheaven.config.Configurator;
import org.middleheaven.config.Parameterized;
import org.middleheaven.core.reflection.ClassLoading;
import org.middleheaven.dependency.Dependency;
import org.middleheaven.dependency.Installable;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.xml.XMLException;
import org.middleheaven.io.xml.XMLObjectContructor;
import org.middleheaven.service.Service;
import org.middleheaven.service.ServiceDependency;

import static org.middleheaven.io.xml.XMLUtils.*;

/**
 * Loads services from XML files located in the Environments Configuration Repository.
 * Service fields are those ending with -services.xml
 * @author Sergio M. M. Taborda 
 *
 */
public class XMLApplicationConfigurator  implements Configurator<ApplicationConfiguration>{

    public XMLApplicationConfigurator(){
    }
    
    public ApplicationConfiguration getConfiguration(ConfigurationContext context) {

        // obtain all application configuration files in the repository
        Collection<? extends ManagedFile> files = context.getExecutionEnvironment().getAppConfigRepository().listFiles(new ManagedFileFilter(){

            public boolean accept(ManagedFile file) {
                return file.getName().endsWith("-application.xml");
            }
            
        });

        // process each file
        List<Installable> masterSet = new ArrayList<Installable>();
        ApplicationInstallContextLoader loader = new ApplicationInstallContextLoader(masterSet);
        
        for (ManagedFile f : files){
            loader.constructFrom(f);
        }
        
        return new ApplicationConfiguration(masterSet);
       
    }
    
    /**
     * Constructs a Set of <code>ServiceInstalments</code> from a XML file
     * @author Sergio M. M. Taborda 
     *
     */
    private class ApplicationInstallContextLoader  extends XMLObjectContructor {
    
        Collection<Installable> masterSet;
        public ApplicationInstallContextLoader(Collection<Installable> masterSet){
            this.masterSet = masterSet;
        }
        
        protected void constructFrom(ManagedFile file) throws ManagedIOException , XMLException{
            super.constructFrom(file);
        }
        
        @Override
        protected void constructFrom(Document document) throws ManagedIOException, XMLException {
            
            Element root = document.getRootElement();
            
            String id = getStringAttribute("id",root);
            
          
           // config.addParameter("id", id);
            
//          TODO resto dos campos
            
            Element info = root.element("info");

            List modules = root.element("modules").elements();
            
            for (Object o : modules){
                processModuleElement((Element)o);
            }
            
           // this.setConstructedObject(config);
            
        }
        
        private void processModuleElement(Element element) {
            String id = getStringAttribute("id", element);
            
            String type = getStringAttribute("type", element, null);
            
            Module module;
            if (type==null || "custom".equals(type)){
                String classe = getStringAttribute("class", element);
                module = (Module)ClassLoading.newClassInstance(classe);
                
            } else {
                // Initializes class from type
                if ("domain".equals(type)){
                    module = new DomainModule();
                }else if ("persistence".equals(type)){
                    module = new PersistenceModule();
                } else {
                    throw new IllegalStateException(type + " is not a valid module type");
                }
            }
            
            ModuleInstallment im = new ModuleInstallment(module);
            
            if (module instanceof Parameterized) {
                Element d = element.element("parameters");
                if (d!=null){
                   ((ParameterizedModule)module).setAllParameters(getParams(d));
                }
            }
            
            
            Element d = element.element("depends");
            if (d!=null){
                List dependencies = d.elements("dependency");
                for (Object o : dependencies){
                    Element ed = (Element)o;
                    String dtype = getStringAttribute("type",ed);
                    
                    Dependency dependency;
                    if ("module".equals(dtype)){
                        dependency = new ModuleDependency(getStringAttribute("id",ed));
                    } else if ("service".equals(dtype)){
                        Class<Service> service = ClassLoading.loadClass(getStringAttribute("class",ed));
                        dependency = new ServiceDependency<Service>(service,false);
                    } else {
                        throw new IllegalStateException(dtype + " is an invalid module dependency type");
                    }
                    dependency.setRequired(booleanAttribute("required",ed,true));
                    im.addDependency(dependency);
                }
            }

            masterSet.add(im);
        }


    
    }

 

}
