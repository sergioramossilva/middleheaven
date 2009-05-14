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
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.ActivatorScanner;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.ServletContextLogBookWriter;
import org.middleheaven.logging.WritableLogBook;
import org.middleheaven.ui.service.UIServiceActivator;
import org.middleheaven.web.container.WebContainerSwitcher;

public class WebContainerBootstrap extends ExecutionEnvironmentBootstrap implements ServletContextListener{


	protected ServletContext servletContext;

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try{
			servletContext =  servletContextEvent.getServletContext();


			start(
					new WritableLogBook("web container book",LoggingLevel.ALL)
					.addWriter(new ServletContextLogBookWriter(servletContext))
			);


			HttpServerService httpService = ServiceRegistry.getService(HttpServerService.class);

			httpService.start();
		}catch (Throwable t){
			servletContextEvent.getServletContext().log("Unexpected error", t);
		}
	}

	protected void doEnvironmentServiceRegistry(WiringService wiringService) {

		HttpServerService httpService = new ServletHttpServerService();
		ServiceRegistry.register(HttpServerService.class, httpService);

	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		stop();
		servletContext = null;
	}

	@Override
	public Container getContainer() {
		return new WebContainerSwitcher().getWebContainer(servletContext);
	}

	public void configuate(WiringService wiringService){
		ActivatorScanner scanner = new SetActivatorScanner()
		.addActivator(MetaInfApplicationServiceActivator.class)
		.addActivator(DynamicLoadApplicationServiceActivator.class)
		.addActivator(UIServiceActivator.class)
		;

		wiringService.addActivatorScanner(scanner);
	}



}
