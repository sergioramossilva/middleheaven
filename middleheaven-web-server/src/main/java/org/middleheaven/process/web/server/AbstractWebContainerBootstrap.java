/**
 * 
 */
package org.middleheaven.process.web.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.middleheaven.aas.AccessControlService;
import org.middleheaven.aas.SimpleAccessControlService;
import org.middleheaven.application.DynamicLoadApplicationServiceActivator;
import org.middleheaven.application.MetaInfApplicationServiceActivator;
import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.ServletContextLogBookWriter;
import org.middleheaven.logging.WritableLogBook;
import org.middleheaven.ui.service.AbstractUIServiceActivator;
import org.middleheaven.ui.web.service.WebUIServiceActivator;
import org.middleheaven.web.container.WebContainer;
import org.middleheaven.web.container.WebContainerInfo;
import org.middleheaven.web.container.WebContainerSwitcher;

/**
 * 
 */
public abstract class AbstractWebContainerBootstrap extends ExecutionEnvironmentBootstrap implements ServletContextListener{

	protected ServletContext servletContext;

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		//servlet hook
		
		servletContext =  servletContextEvent.getServletContext();
		
		servletContext.log("[MiddleHeaven] Initializing " + this.getClass().getSimpleName() + " at " + servletContext.getContextPath());
		
		try{
			
			start(
					new WritableLogBook("",LoggingLevel.ALL)
					.addWriter(new ServletContextLogBookWriter(servletContext))
			);
			
		
			HttpServerService httpService = ServiceRegistry.getService(HttpServerService.class);

			httpService.start();
			servletContext.log("[MiddleHeaven] Bootstap completed");
		}catch (Throwable t){
			servletContext.log("[MiddleHeaven] Unexpected error", t);
		}
	}
	
	@Override
	protected void preConfig(BootstrapContext context) {
		// web application have a special inicialization

		context.addActivator(MetaInfApplicationServiceActivator.class)
		.addActivator(DynamicLoadApplicationServiceActivator.class)
		.addActivator(WebUIServiceActivator.class);
		
		ServletHttpServerService httpService = new ServletHttpServerService();
		
		// hook for Frontend servlet
		this.servletContext.setAttribute("httpService", httpService);

		ServiceRegistry.register(HttpServerService.class, httpService);

		// access service
		ServiceRegistry.register(AccessControlService.class, new SimpleAccessControlService());
	}
	

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		stop();
		servletContext = null;
	}

	@Override
	public BootstrapContainer resolveContainer(ManagedFile rooFolder) {
		WebContainer container = new WebContainerSwitcher().getWebContainer(servletContext, rooFolder);
		
		this.servletContext.setAttribute(WebContainerInfo.ATTRIBUTE_NAME, container.getWebContainerInfo());
		
		return container;
	}



}
