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
    
    @Override
	public ManagedFile getAppRootRepository() {
		return repository;
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
        return repository;
    }
    
    @Override
	public ManagedFile getEnvironmentDataRepository() {
    	  return repository.retrive("data");
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
