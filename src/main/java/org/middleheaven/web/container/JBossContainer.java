/*
 * Created on 2006/09/02
 *
 */
package org.middleheaven.web.container;

import java.io.File;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepositories;

/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class JBossContainer extends WebContainer {

    public JBossContainer(ServletContext context){
       super(context);
    }
    
 
    
    @Override
    public String getEnvironmentName() {
        // TODO add more details like System: versao , OS , etc 
        return "JBoss";
    }

    @Override
	public void stop(ExecutionEnvironmentBootstrap bootstrap) {
		// TODO Auto-generated method stub
		
	}
    
    @Override
    public void init(ExecutionEnvironmentBootstrap bootstrap) {
 
        // Set JNDI default parameters
        System.setProperty("java.naming.factory.initial",  "org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url", "localhost:1099");
        
    }


    public ManagedFile getEnvironmentConfigRepository() {
        return getEnvironmentDeployRepository();
    }

    public ManagedFile getEnvironmentDeployRepository() {
        return ManagedFileRepositories.resolveFile(new File(System.getProperty("jboss.server.home.dir") + File.separator + "deploy"));
    }

    public ManagedFile getAppConfigRepository() {
        return ManagedFileRepositories.resolveFile(new File(System.getProperty("jboss.server.home.dir") + File.separator + "conf"));
    }

    public ManagedFile getAppDataRepository() {
        return ManagedFileRepositories.resolveFile(new File(System.getProperty("jboss.server.home.dir") + File.separator + "app-data"));
    }

    public ManagedFile getAppLogRepository() {
        return ManagedFileRepositories.resolveFile(new File(System.getProperty("jboss.server.home.dir") + File.separator + "log"));
    }

    public ManagedFile getAppClasspathRepository() {
        return null; // TODO usar ClasspathRepository
    }
    
}
