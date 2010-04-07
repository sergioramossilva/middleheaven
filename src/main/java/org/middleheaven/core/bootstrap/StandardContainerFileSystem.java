package org.middleheaven.core.bootstrap;

import org.middleheaven.io.repository.ManagedFile;

public class StandardContainerFileSystem implements ContainerFileSystem{

	private ManagedFile root;

	public StandardContainerFileSystem(ManagedFile root){
		this.root = root;
	}
	
    public ManagedFile getEnvironmentConfigRepository() {
        return getAppConfigRepository();
    }

    public ManagedFile getEnvironmentDeployRepository() {
        return getAppConfigRepository();
    }

    public ManagedFile getAppConfigRepository() {
        return root.retrive("META-INF");
    }

    public ManagedFile getAppDataRepository() {
        return root.retrive("data");
    }

    public ManagedFile getAppLogRepository() {
        return root.retrive("logs");
    }

    public ManagedFile getAppClasspathRepository() {
        return root.retrive("bin");
    }
    
	@Override
	public ManagedFile getEnvironmentDataRepository() {
		 return root.retrive("data");
	}
	
	@Override
	public ManagedFile getAppRootRepository() {
		return root;
	}
}
