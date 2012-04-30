package org.middleheaven.core.bootstrap;

import org.middleheaven.io.repository.ManagedFile;

public class StandardContainerFileSystem implements FileContext{

	private ManagedFile rootFolder;

	public StandardContainerFileSystem(ManagedFile rootFolder){
		this.rootFolder = rootFolder;
	}
	
    public ManagedFile getEnvironmentConfigRepository() {
        return getAppConfigRepository();
    }

    public ManagedFile getEnvironmentDeployRepository() {
        return getAppConfigRepository();
    }

    public ManagedFile getAppConfigRepository() {
        return rootFolder.retrive("/META-INF");
    }

    public ManagedFile getAppDataRepository() {
        return rootFolder.retrive("/data");
    }

    public ManagedFile getAppLogRepository() {
    	 return rootFolder.retrive("/logs");
    }

    public ManagedFile getAppClasspathRepository() {
        return rootFolder.retrive("/bin");
    }
    
	@Override
	public ManagedFile getEnvironmentDataRepository() {
		 return rootFolder.retrive("/data");
	}
	
	@Override
	public ManagedFile getAppRootRepository() {
		 return rootFolder;
	}
}
