/*
 * Created on 2006/09/02
 *
 */
package org.middleheaven.web.container;

import java.io.File;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.machine.MachineFiles;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.jndi.JNDINameDirectoryService;

/**
 * 
 */
public abstract class AbstractJBossContainer extends StandardSevletContainer {

    public AbstractJBossContainer(ServletContext context, ManagedFile root){
       super(context, root);
    }

    @Override
    public String getContainerName() {
        // TODO add more details like System: versao , OS , etc 
        return "JBoss";
    }

    
    @Override
    public void configurate(BootstrapContext context) {
 
        // Set JNDI default parameters
        System.setProperty("java.naming.factory.initial",  "org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url", "localhost:1099");
        
  
        JNDINameDirectoryService service = new JNDINameDirectoryService();
        
        ServiceRegistry.register(NameDirectoryService.class, service);
        
        
    }


    public ManagedFile getEnvironmentConfigRepository() {
        return getEnvironmentDeployRepository();
    }

    public ManagedFile getEnvironmentDeployRepository() {
        return MachineFiles.resolveFile(new File(System.getProperty("jboss.server.home.dir") , "deploy"));
    }

    public ManagedFile getAppConfigRepository() {
        return MachineFiles.resolveFile(new File(System.getProperty("jboss.server.home.dir") , "conf"));
    }

    public ManagedFile getAppDataRepository() {
        return MachineFiles.resolveFile(new File(System.getProperty("jboss.server.home.dir") ,"app-data"));
    }

    public ManagedFile getAppLogRepository() {
        return MachineFiles.resolveFile(new File(System.getProperty("jboss.server.home.dir") , "log"));
    }

    public ManagedFile getAppClasspathRepository() {
        return null; // TODO usar ClasspathRepository
    }
    
}
