/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.web.container;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.EditableContainerFileSystem;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.machine.MachineFiles;
import org.middleheaven.namedirectory.jndi.JNDINamingDirectoryActivator;
import org.middleheaven.process.web.CommonHttpServerContainers;
import org.middleheaven.util.PropertiesBag;
import org.middleheaven.util.Version;

/**
 * 
 * @see WebContainer
 */
public class CatalinaContainer extends StandardSevletContainer  {


	public CatalinaContainer(ServletContext context, ManagedFile root){
		super(context, root);
	}

	@Override
	public void configurate(BootstrapContext context) {
		super.configurate(context);
		
		Boolean useNaming = PropertiesBag.bagOfSystemProperties().getProperty("catalina.useNaming", Boolean.FALSE, Boolean.class);
		
		if(useNaming.booleanValue()){
			context.addActivator(JNDINamingDirectoryActivator.class);
		}


	}

	@Override
	protected void setupDefaultFilesRepositories(ServletContext context,EditableContainerFileSystem fileSystem){
	
		String catalinaBasePath = PropertiesBag.bagOfSystemProperties().getProperty("catalina.base", String.class);
		
		File catalinaBase = new File(catalinaBasePath);

		fileSystem.setEnvironmentConfigRepository(MachineFiles.resolveFile(new File(catalinaBase ,  "conf")));
		fileSystem.setEnvironmentDataRepository(MachineFiles.resolveFile(new File(catalinaBase , "data")).createFolder());

		super.setupDefaultFilesRepositories(context, fileSystem);
		
	}

	@Override
	public String getContainerName() {
		return CommonHttpServerContainers.TOMCAT.name();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebContainerInfo getWebContainerInfo() {
		ClassIntrospector<?> introspector = ClassIntrospector.loadFrom("org.apache.catalina.util.ServerInfo");
		try {
			String number = (String) introspector.inspect().methods().beingStatic(true).named("getServerNumber").retrive().invoke(null, new Object[0]);
			String serverBuilt = (String) introspector.inspect().methods().beingStatic(true).named("getServerBuilt").retrive().invoke(null, new Object[0]);

			number = number  + "." + serverBuilt;
			
			return new WebContainerInfo(CommonHttpServerContainers.TOMCAT, Version.valueOf(number));
			
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e, introspector.getIntrospected());
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e, introspector.getIntrospected());
		} catch (InvocationTargetException e) {
			throw ReflectionException.manage(e, introspector.getIntrospected());
		}
	}






}
