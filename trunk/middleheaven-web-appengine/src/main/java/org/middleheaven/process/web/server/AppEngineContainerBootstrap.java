/**
 * 
 */
package org.middleheaven.process.web.server;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.web.container.WebContainer;
import org.middleheaven.web.container.WebContainerInfo;

/**
 * 
 */
public class AppEngineContainerBootstrap extends AbstractWebContainerBootstrap {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile getEnvironmentRootFolder() {
		// TODO uso blob api
		return null;
	}

	@Override
	public final BootstrapContainer resolveContainer(ManagedFile rootFolder) {
		WebContainer container =  new AppEngineWebContainer(this.servletContext , rootFolder);
			
		this.servletContext.setAttribute(WebContainerInfo.ATTRIBUTE_NAME, container.getWebContainerInfo());
		
		return container;
	}
}
