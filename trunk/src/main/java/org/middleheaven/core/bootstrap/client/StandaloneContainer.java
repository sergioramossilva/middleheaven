/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.BootstrapContainer;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.repository.ManagedFile;

/**
 *
 */
public class StandaloneContainer implements BootstrapContainer {

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
        return repository.retrive("META-INF");
    }

    public ManagedFile getAppDataRepository() {
        return repository.retrive("data");
    }

    public ManagedFile getAppLogRepository() {
        return repository.retrive("logs");
    }

    public ManagedFile getAppClasspathRepository() {
        return repository.retrive("bin");
    }
    
	@Override
	public ManagedFile getEnvironmentDataRepository() {
		 return repository.retrive("data");
	}
	
	@Override
	public ManagedFile getAppRootRepository() {
		return repository;
	}
	
    public void init(WiringService wiringService){}

    @Override
	public void start() {}

	@Override
	public void stop() {}




}
