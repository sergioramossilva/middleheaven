package org.middleheaven.web.container;

import javax.servlet.ServletContext;

import org.middleheaven.core.reflection.inspection.ClassIntrospector;

/**
 * Determines the container from the current {@link ServletContext}.
 */
public class WebContainerSwitcher {

	/**
	 * Determines the container from the current {@link ServletContext}.
	 * @param servletContext
	 * @param rootFolder 
	 * @return
	 */
	public WebContainer getWebContainer(ServletContext servletContext){
		
	
		String serverInfo = servletContext.getServerInfo();
		
		if (serverInfo.contains("Google App Engine")){
			// it's App Engine
			
			final String className = "org.middleheaven.process.web.server.AppEngineWebContainer";
			if (ClassIntrospector.isInClasspath(className)){
				return ClassIntrospector.of(WebContainer.class).load(className).newInstance(servletContext);
			} else {
				// TODO warning that the corret container was not found
				return new StandardSevletContainer(servletContext);
			}
			
			
		} else {

			// if there exists a property "jboss.server.home.url"
			// we are inside jboss; else assume we are in Tomcat only
			String jbossHome = System.getProperty("jboss.server.home.url");
		
			if (jbossHome==null){
				// can assume Tomcat ?
				String tomcatHome = System.getProperty("catalina.home");
				if (tomcatHome==null){
					// cannot
					return new StandardSevletContainer(servletContext);
				} else {
					return new CatalinaContainer(servletContext);
				}
			} else {
				// inside JBoss
				return  new StandardJBossContainer(servletContext);
			}
		}

		
		
	}
}
