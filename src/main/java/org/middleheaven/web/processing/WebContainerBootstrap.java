/*
 * Created on 2006/09/02
 *
 */
package org.middleheaven.web.processing;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.ServletContextLogBookWriter;
import org.middleheaven.logging.WritableLogBook;
import org.middleheaven.web.container.WebContainerSwitcher;

public class WebContainerBootstrap extends ExecutionEnvironmentBootstrap implements ServletContextListener{


	protected ServletContext servletContext;

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		servletContext =  servletContextEvent.getServletContext();
		servletContext.setAttribute("application.id", this.getContextIdentifier());
		
		ServiceRegistry.register(HTTPServerService.class, new ServletHttpServerService(servletContext));
		
		start(
				new WritableLogBook("web container book",LoggingLevel.ALL)
				.addWriter(new ServletContextLogBookWriter(servletContext))
		);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		stop();
		servletContext = null;
	}


	@Override
	public ContextIdentifier getContextIdentifier() {
		// get it from a parameter in the configuration
		return ContextIdentifier.getInstance((String)servletContext.getInitParameter("application.id"));
	}

	@Override
	public Container getContainer() {
		return new WebContainerSwitcher().getWebContainer(servletContext);
	}




}
