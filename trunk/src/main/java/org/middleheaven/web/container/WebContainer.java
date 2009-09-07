package org.middleheaven.web.container;

import java.io.File;

import javax.servlet.ServletContext;

import org.middleheaven.core.BootstrapContainer;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFiles;

public abstract class WebContainer implements BootstrapContainer  {

	private ServletContext context;
	public WebContainer(ServletContext context) {
		this.context = context;
	}

	public ServletContext getServletContext(){
		return this.context;
	}

	@Override
	public void start() {
		// no-op
	}

	@Override
	public void stop(){
		this.context = null; 
	}

	/**
	 * @return <code>ManagedFileRepository</code> representing the Repository where application configuration files are stored. 
	 * Normally this points to the META-INF folder for war applications
	 */
	public ManagedFile getAppConfigRepository() {
		return ManagedFiles.resolveFile(new File(context.getRealPath("./META-INF")));
	}

	/**
	 * @return <code>ManagedFileRepository</code> representing the Repository where environment configuration files are stored. 
	 * Normally this points to the WEB-INF folder for war applications
	 */
	@Override
	public ManagedFile getEnvironmentConfigRepository() {
		return ManagedFiles.resolveFile(new File(context.getRealPath("./WEB-INF")));
	}

	/**
	 * @return <code>ManagedFileRepository</code> representing the repository where the application can store data
	 * This defaults to the application`s root web folder. Application are encouraged to use specific folders inside the root
	 */
	public ManagedFile getAppDataRepository() {
		return ManagedFiles.resolveFile(new File(context.getRealPath(".")));
	}

	/**
	 * @return <code>ManagedFileRepository</code> representing the repository where the application can store their logs
	 * This defaults to the application`s root web folder. Application are encouraged to use specific folders inside the root
	 */
	public ManagedFile getAppLogRepository() {
		return ManagedFiles.resolveFile(new File(context.getRealPath(".")));
	}



	public ManagedFile getAppClasspathRepository() {
		return ManagedFiles.resolveFile(new File(context.getRealPath("./WEB-INF/classes")));
	}


	public void start(ExecutionEnvironmentBootstrap bootstrap){}



}
