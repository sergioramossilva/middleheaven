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
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.ServletContextLogBookWriter;
import org.middleheaven.logging.WritableLogBook;
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
			
		
			
			final ServletHttpServerService httpService = this.getServletHttpServerService();
			final AccessControlService accessControlService = this.getAccessControlService();

			this.getWiringService().getObjectPool().addConfiguration(new BindConfiguration(){

				@Override
				public void configure(Binder binder) {
					
					binder.bind(HttpServerService.class).in(Service.class).toInstance(httpService);
					binder.bind(AccessControlService.class).in(Service.class).toInstance(accessControlService);
				}
				
			});

			// hook for Frontend servlet
			this.servletContext.setAttribute("httpService", httpService);
			
			start(
					new WritableLogBook("",LoggingLevel.ALL)
					.addWriter(new ServletContextLogBookWriter(servletContext))
			);
			
			
			httpService.start();
			
			servletContext.log("[MiddleHeaven] Bootstap completed");
		}catch (Throwable t){
			servletContext.log("[MiddleHeaven] Unexpected error", t);
		}
	}
	
	protected ServletHttpServerService getServletHttpServerService(){
		return new ServletHttpServerService();	
	}
	
	protected AccessControlService getAccessControlService(){
		return new StandardAccessControlService();
	}
	
	@Override
	protected void preConfig(BootstrapContext context) {
		// web application have a special inicialization

		context.addActivator(MetaInfApplicationServiceActivator.class)
		.addActivator(DynamicLoadApplicationServiceActivator.class)
		.addActivator(WebUIServiceActivator.class);

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
