package org.middleheaven.core;

import java.io.File;

import javax.servlet.ServletContext;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepositories;

public abstract class WebContainer implements Container  {

	protected ServletContext context;
	public WebContainer(ServletContext context) {
		this.context = context;
	}
	
	 /**
     * @return <code>ManagedFileRepository</code> representing the Repository where application configuration files are stored. 
     * Normally this points to the WEB-INF folder for war applications
     */
    public ManagedFile getAppConfigRepository() {
        return ManagedFileRepositories.resolveFile(new File(context.getRealPath("./WEB-INF")));
    }
    

	@Override
	public ManagedFile getEnvironmentConfigRepository() {
		return getAppConfigRepository();
	}

    /**
     * @return <code>ManagedFileRepository</code> representing the repository where the application can store data
     * This defaults to the application`s root web folder. Application are encouraged to use specific folders inside the root
     */
    public ManagedFile getAppDataRepository() {
        return ManagedFileRepositories.resolveFile(new File(context.getRealPath(".")));
    }

    /**
     * @return <code>ManagedFileRepository</code> representing the repository where the application can store their logs
     * This defaults to the application`s root web folder. Application are encouraged to use specific folders inside the root
     */
    public ManagedFile getAppLogRepository() {
        return ManagedFileRepositories.resolveFile(new File(context.getRealPath(".")));
    }


   
    public ManagedFile getAppClasspathRepository() {
        return ManagedFileRepositories.resolveFile(new File(context.getRealPath("./WEB-INF/classes")));
    }
    



}
