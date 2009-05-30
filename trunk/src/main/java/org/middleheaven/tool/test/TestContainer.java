package org.middleheaven.tool.test;

import org.middleheaven.core.BootstrapContainer;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.repository.ManagedFile;

public class TestContainer implements BootstrapContainer {

	private ManagedFile repository;

	protected TestContainer( ManagedFile repository){
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
        return repository;
    }
    
	@Override
	public void start() {
		// TODO implement Container.start
		
	}

	@Override
	public void stop() {
		// TODO implement Container.stop
		
	}

	@Override
	public void init(WiringService wiringService) {
		// TODO implement BootstrapContainer.init
		
	}

}
