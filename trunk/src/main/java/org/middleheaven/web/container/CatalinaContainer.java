/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.web.container;

import java.io.File;

import javax.servlet.ServletContext;

import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.io.repository.ManagedFiles;
import org.middleheaven.namedirectory.jndi.JNDINamingDirectoryActivator;
import org.middleheaven.work.scheduled.AlarmClockScheduleWorkExecutionServiceActivator;

/**
 * 
 * @see WebContainer
 */
public class CatalinaContainer extends WebContainer  {


	public CatalinaContainer(ServletContext context){
		super(context);
	}

	@Override
	public void init(WiringService wiringService) {
		super.init(wiringService);
		SetActivatorScanner scanner = new SetActivatorScanner();
		scanner.addActivator(AlarmClockScheduleWorkExecutionServiceActivator.class);
		
		Boolean useNaming = Boolean.valueOf(System.getProperty("catalina.useNaming"));
		
		if(useNaming.booleanValue()){
			scanner.addActivator(JNDINamingDirectoryActivator.class);
		}
		
		wiringService.addActivatorScanner(scanner);

	}

	protected void setupDefaultFilesRepositories(ServletContext context){
	
		
		File catalinaBase = new File(System.getProperty("catalina.base"));

		setEnvironmentConfigRepository(ManagedFiles.resolveFile(new File(catalinaBase ,  "conf")));
		setEnvironmentDataRepository(ManagedFiles.resolveFile(new File(catalinaBase , "data")).createFolder());

		super.setupDefaultFilesRepositories(context);
		
	}

	@Override
	public String getEnvironmentName() {
		// TODO get detais, version, OS , etc..
		return "Tomcat";
	}






}
