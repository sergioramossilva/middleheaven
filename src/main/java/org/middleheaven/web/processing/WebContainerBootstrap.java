/*
 * Created on 2006/09/02
 *
 */
package org.middleheaven.web.processing;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.middleheaven.application.DynamicLoadApplicationServiceActivator;
import org.middleheaven.application.MetaInfApplicationServiceActivator;
import org.middleheaven.core.BootstrapContainer;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.ActivatorScanner;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.ServletContextLogBookWriter;
import org.middleheaven.logging.WritableLogBook;
import org.middleheaven.logging.writters.ConsoleLogWriter;
import org.middleheaven.ui.service.UIServiceActivator;
import org.middleheaven.web.container.WebContainerSwitcher;

public class WebContainerBootstrap extends ExecutionEnvironmentBootstrap implements ServletContextListener{


	protected ServletContext servletContext;

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		
		servletContext =  servletContextEvent.getServletContext();
		
		servletContext.log("[MiddleHeaven] Initializing " + this.getClass().getSimpleName() + " at " + servletContext.getContextPath());
		
		try{
			

			start(
					new WritableLogBook("",LoggingLevel.ALL)
					.addWriter(new ConsoleLogWriter())
					.addWriter(new ServletContextLogBookWriter(servletContext))
			);


			HttpServerService httpService = ServiceRegistry.getService(HttpServerService.class);

			httpService.start();
			servletContext.log("[MiddleHeaven] Bootstap completed");
		}catch (Throwable t){
			servletContext.log("[MiddleHeaven] Unexpected error", t);
		}
	}

	protected void doEnvironmentServiceRegistry(WiringService wiringService) {
		// web application have a special inicialization
		ActivatorScanner scanner = new SetActivatorScanner()
		.addActivator(MetaInfApplicationServiceActivator.class);
		
		wiringService.addActivatorScanner(scanner);
		
		ServletHttpServerService httpService = new ServletHttpServerService();
		
		// hook for Frontend servlet
		this.servletContext.setAttribute("httpService", httpService);

		ServiceRegistry.register(HttpServerService.class, httpService);

	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		stop();
		servletContext = null;
	}

	@Override
	public BootstrapContainer getContainer() {
		return new WebContainerSwitcher().getWebContainer(servletContext);
	}

	public void configurate(WiringService wiringService){
		ActivatorScanner scanner = new SetActivatorScanner()
		.addActivator(MetaInfApplicationServiceActivator.class)
		.addActivator(DynamicLoadApplicationServiceActivator.class)
		.addActivator(UIServiceActivator.class)
		;

		wiringService.addActivatorScanner(scanner);
	}



}
