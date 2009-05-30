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
    
    public void init(WiringService wiringService){}

    @Override
	public void start() {}

	@Override
	public void stop() {}
}
