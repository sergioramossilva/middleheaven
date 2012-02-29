/**
 * 
 */
package org.middleheaven.process.web.server;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.web.container.WebContainer;
import org.middleheaven.web.container.WebContainerInfo;

/**
 * 
 */
public class AppEngineContainerBootstrap extends AbstractWebContainerBootstrap {

	@Override
	public final BootstrapContainer resolveContainer() {
		WebContainer container =  new AppEngineWebContainer(this.servletContext);
			
		this.servletContext.setAttribute(WebContainerInfo.ATTRIBUTE_NAME, container.getWebContainerInfo());
		
		return container;
	}
}
