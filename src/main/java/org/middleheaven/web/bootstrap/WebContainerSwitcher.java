package org.middleheaven.web.bootstrap;

import javax.servlet.ServletContext;

import org.middleheaven.core.Container;

public class WebContainerSwitcher {

	
	public Container getWebContainer(ServletContext servletContext){
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
				return new TomcatContainer(servletContext);
			}
		} else {
			// inside JBoss
			return  new StandardJBossContainer(servletContext);
		}
	}
}
