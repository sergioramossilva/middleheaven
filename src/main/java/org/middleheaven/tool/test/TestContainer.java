package org.middleheaven.tool.test;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFile;

public class TestContainer implements Container {

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
    
    public void init(ExecutionEnvironmentBootstrap bootstrap){}

	@Override
	public void start(ExecutionEnvironmentBootstrap bootstrap) {
		// TODO implement Container.start
		
	}

	@Override
	public void stop(ExecutionEnvironmentBootstrap bootstrap) {
		// TODO implement Container.stop
		
	}

}
