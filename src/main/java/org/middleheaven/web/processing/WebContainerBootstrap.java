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
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.ServletContextLogBookWriter;
import org.middleheaven.logging.WritableLogBook;
import org.middleheaven.ui.UIServiceActivator;
import org.middleheaven.web.container.WebContainerSwitcher;

public class WebContainerBootstrap extends ExecutionEnvironmentBootstrap implements ServletContextListener{


	protected ServletContext servletContext;

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		servletContext =  servletContextEvent.getServletContext();

		HttpServerService httpService = new ServletHttpServerService();
		
		
		start(
				new WritableLogBook("web container book",LoggingLevel.ALL)
				.addWriter(new ServletContextLogBookWriter(servletContext))
		);
		
		ServiceRegistry.register(HttpServerService.class, httpService);
		
		
		httpService.start();
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		stop();
		servletContext = null;
	}

	@Override
	public Container getContainer() {
		return new WebContainerSwitcher().getWebContainer(servletContext);
	}
	
	public void configuate(ServiceContextConfigurator configurator){
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine()
		.addActivator(MetaInfApplicationServiceActivator.class)
		.addActivator(DynamicLoadApplicationServiceActivator.class)
		.addActivator(UIServiceActivator.class)
		;
		
		configurator.addEngine(engine);
	}



}
