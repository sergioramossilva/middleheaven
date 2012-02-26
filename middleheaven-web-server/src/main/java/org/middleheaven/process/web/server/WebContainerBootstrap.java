
package org.middleheaven.process.web.server;

import java.io.File;

import javax.servlet.ServletContextListener;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.machine.MachineFiles;

/**
 * Bootstrap that run in a web container as a {@link ServletContextListener}.
 */
public class WebContainerBootstrap extends AbstractWebContainerBootstrap {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile getEnvironmentRootFolder() {
		return MachineFiles.resolveFile(new File(this.servletContext.getRealPath("")));
	}


}
