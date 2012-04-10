/**
 * 
 */
package org.middleheaven.process.web.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.middleheaven.aas.AccessControlService;
import org.middleheaven.aas.StandardAccessControlService;
import org.middleheaven.application.DynamicLoadApplicationServiceActivator;
import org.middleheaven.application.MetaInfApplicationServiceActivator;
import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.ExecutionContext;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.wiring.PropertiesPropertyManager;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.logging.ServletContextLogListener;
import org.middleheaven.ui.web.service.WebUIServiceActivator;
import org.middleheaven.web.container.WebContainer;
import org.middleheaven.web.container.WebContainerInfo;
import org.middleheaven.web.container.WebContainerSwitcher;

/**
 * 
 */
public class WebContainerBootstrap extends ExecutionEnvironmentBootstrap implements ServletContextListener{

	private ServletContext servletContext;
	private ServletHttpServerService httpService;
	
	@Override
	protected final void doBeforeStart(){
		httpService = new ServletHttpServerService();	
		final AccessControlService accessControlService = new StandardAccessControlService();

		

		this.getRegistryServiceContext().register(AccessControlService.class, accessControlService, null);
		this.getRegistryServiceContext().register(HttpServerService.class, httpService, null);
		
		PropertiesPropertyManager mhEnvironment = PropertiesPropertyManager.loadFrom(
				this.resolveContainer().getFileSystem().getEnvironmentConfigRepository().retrive("mh_environment.properties")
		);
		
		this.getWiringService().getPropertyManagers().addBefore("system.properties", mhEnvironment);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		//servlet hook
		
		servletContext =  servletContextEvent.getServletContext();
		
		servletContext.log("[MiddleHeaven] Initializing " + this.getClass().getSimpleName() + " at " + servletContext.getContextPath());
		
	
		try{


			
			
//			start(
//					new WritableLogBook("",LoggingLevel.ALL)
//					.addWriter(new ServletContextLogBookWriter(servletContext))
//			);
			
			// TODO (?) inicialize with ConfigurableListener refering the ServletContextLogBookWriter
			this.start();
			
	
			// hook for Frontend servlet
			this.servletContext.setAttribute("httpService", httpService);
			
			
			httpService.start();
			
			servletContext.log("[MiddleHeaven] Bootstap completed");
		}catch (Throwable t){
			servletContext.log("[MiddleHeaven] Unexpected error", t);
		}
	}


	
	@Override
	protected void preConfig(ExecutionContext context) {
		// web application have a special initialization
		
		context.addActivator(MetaInfApplicationServiceActivator.class)
			.addActivator(DynamicLoadApplicationServiceActivator.class)
			.addActivator(WebUIServiceActivator.class);

		
	}
	

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		stop();
		servletContext = null;
	}

	@Override
	public BootstrapContainer resolveContainer() {
		WebContainer container = new WebContainerSwitcher().getWebContainer(servletContext);
		
		this.servletContext.setAttribute(WebContainerInfo.ATTRIBUTE_NAME, container.getWebContainerInfo());
		
		return container;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getExecutionConfiguration() {
		return servletContext.getInitParameter("configuration");
	}



}
