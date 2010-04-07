/*
 * Created on 2006/09/02
 *
 */
package org.middleheaven.web.container;

import java.io.File;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFiles;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.jndi.JNDINameDirectoryService;

/**
 * 
 */
public abstract class AbstractJBossContainer extends StandardSevletContainer {

    public AbstractJBossContainer(ServletContext context){
       super(context);
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
        
        // TODO register
        JNDINameDirectoryService service = new JNDINameDirectoryService();
        
        ServiceRegistry.register(NameDirectoryService.class, new JNDINameDirectoryService());
        
        
    }


    public ManagedFile getEnvironmentConfigRepository() {
        return getEnvironmentDeployRepository();
    }

    public ManagedFile getEnvironmentDeployRepository() {
        return ManagedFiles.resolveFile(new File(System.getProperty("jboss.server.home.dir") , "deploy"));
    }

    public ManagedFile getAppConfigRepository() {
        return ManagedFiles.resolveFile(new File(System.getProperty("jboss.server.home.dir") , "conf"));
    }

    public ManagedFile getAppDataRepository() {
        return ManagedFiles.resolveFile(new File(System.getProperty("jboss.server.home.dir") ,"app-data"));
    }

    public ManagedFile getAppLogRepository() {
        return ManagedFiles.resolveFile(new File(System.getProperty("jboss.server.home.dir") , "log"));
    }

    public ManagedFile getAppClasspathRepository() {
        return null; // TODO usar ClasspathRepository
    }
    
}
