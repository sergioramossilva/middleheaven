/**
 * 
 */
package org.middleheaven.process.web.server;

import javax.servlet.ServletContext;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
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
	public AppEngineWebContainer(ServletContext context) {
		super(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContainerName() {
		return "AppEngine";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebContainerInfo getWebContainerInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepositoryProvider getManagedFileRepositoryProvider() {
		// determine aapengine file system
		throw new UnsupportedOperationException("Not implememented yet");
	}

}
