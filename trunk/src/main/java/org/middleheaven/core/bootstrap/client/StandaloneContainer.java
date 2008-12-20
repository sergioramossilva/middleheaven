/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFile;

/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class StandaloneContainer implements Container {

	private ManagedFile repository;

	protected StandaloneContainer( ManagedFile repository){
		this.repository = repository;
	}
	
    @Override
    public String getEnvironmentName() {
        return "standalone";
    }
    

    public ManagedFile getEnvironmentConfigRepository() {
        return getAppConfigRepository();
    }

    public ManagedFile getEnvironmentDeployRepository() {
        return getAppConfigRepository();
    }

    public ManagedFile getAppConfigRepository() {
        return repository.resolveFile("META-INF");
    }

    public ManagedFile getAppDataRepository() {
        return repository.resolveFile("data");
    }

    public ManagedFile getAppLogRepository() {
        return repository.resolveFile("logs");
    }

    public ManagedFile getAppClasspathRepository() {
        return repository.resolveFile("bin");
    }
    
    public void init(ExecutionEnvironmentBootstrap bootstrap){}


}
