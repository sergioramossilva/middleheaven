/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.web.container;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.EditableContainerFileRepositoryManager;
import org.middleheaven.core.bootstrap.ExecutionContext;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.io.repository.machine.MachineFiles;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.logging.ServletContextLogListener;
import org.middleheaven.namedirectory.jndi.JNDINamingDirectoryActivator;
import org.middleheaven.process.web.CommonHttpServerContainers;
import org.middleheaven.util.PropertiesBag;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.Version;

/**
 * 
 * @see WebContainer
 */
public class CatalinaContainer extends StandardSevletContainer  {


	public CatalinaContainer(ServletContext context){
		super(context);
	}

	@Override
	public void configurate(ExecutionContext context) {
		super.configurate(context);
		
		Boolean useNaming = PropertiesBag.bagOfSystemProperties().getProperty("catalina.useNaming", Boolean.FALSE, Boolean.class);
		
		if(useNaming.booleanValue()){
			context.addActivator(JNDINamingDirectoryActivator.class);

	
		}

		final ServletContext servletContext = this.getServletContext();
		final ServletContextLogListener listener = new ServletContextLogListener(servletContext);
		
		String level = servletContext.getInitParameter("logging.level");
		
		if (!StringUtils.isEmptyOrBlank(level)){
			listener.setLevel(LoggingLevel.valueOf(level.toUpperCase()));
		} else {
			listener.setLevel(LoggingLevel.WARN);
		}
		
		context.getServiceContext().getService(LoggingService.class).addLogListener(listener);
	}

	@Override
	protected void setupDefaultFilesRepositories(ServletContext context,EditableContainerFileRepositoryManager fileSystem){
	
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
