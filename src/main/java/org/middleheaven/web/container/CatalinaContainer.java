/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.web.container;

import java.io.File;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.ContainerFileSystem;
import org.middleheaven.core.bootstrap.EditableContainerFileSystem;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.io.repository.ManagedFiles;
import org.middleheaven.namedirectory.jndi.JNDINamingDirectoryActivator;
import org.middleheaven.work.scheduled.AlarmClockScheduleWorkExecutionServiceActivator;

/**
 * 
 * @see WebContainer
 */
public class CatalinaContainer extends StandardSevletContainer  {


	public CatalinaContainer(ServletContext context){
		super(context);
	}

	@Override
	public void configurate(BootstrapContext context) {
		super.configurate(context);
		
		context.addActivator(AlarmClockScheduleWorkExecutionServiceActivator.class);
		
		Boolean useNaming = Boolean.valueOf(System.getProperty("catalina.useNaming"));
		
		if(useNaming.booleanValue()){
			context.addActivator(JNDINamingDirectoryActivator.class);
		}


	}

	@Override
	protected void setupDefaultFilesRepositories(ServletContext context,EditableContainerFileSystem fileSystem){
	
		
		File catalinaBase = new File(System.getProperty("catalina.base"));

		fileSystem.setEnvironmentConfigRepository(ManagedFiles.resolveFile(new File(catalinaBase ,  "conf")));
		fileSystem.setEnvironmentDataRepository(ManagedFiles.resolveFile(new File(catalinaBase , "data")).createFolder());

		super.setupDefaultFilesRepositories(context, fileSystem);
		
	}

	@Override
	public String getContainerName() {
		// TODO get detais, version, OS , etc..
		return "Tomcat";
	}









}
