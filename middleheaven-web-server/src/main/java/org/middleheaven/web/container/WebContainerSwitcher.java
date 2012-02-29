package org.middleheaven.web.container;

import javax.servlet.ServletContext;

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
