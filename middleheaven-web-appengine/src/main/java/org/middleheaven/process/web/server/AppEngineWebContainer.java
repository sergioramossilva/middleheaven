/**
 * 
 */
package org.middleheaven.process.web.server;

import javax.servlet.ServletContext;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.web.container.WebContainer;
import org.middleheaven.web.container.WebContainerInfo;

/**
 * 
 */
public class AppEngineWebContainer extends WebContainer {

	/**
	 * Constructor.
	 * @param context
	 * @param root
	 */
	public AppEngineWebContainer(ServletContext context, ManagedFile root) {
		super(context, root);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContainerName() {
		return "App Engine Container";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebContainerInfo getWebContainerInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
