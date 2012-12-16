/**
 * 
 */
package org.middleheaven.process.web.server;

import javax.servlet.ServletContext;

import org.middleheaven.application.ApplicationModulesResolver;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.web.container.WebContainerBootstrapEnvironment;
import org.middleheaven.web.container.WebContainerInfo;

/**
 * 
 */
public class AppEngineWebContainer extends WebContainerBootstrapEnvironment {

	/**
	 * Constructor.
	 * @param context
	 * @param root
	 */
	public AppEngineWebContainer(ServletContext context) {
		super(context);
		
		String serverInfo = context.getServerInfo();
		
		if (serverInfo.contains("Google App Engine")){
			// it's App Engine
			
			/* ServletContext.getServerInfo() will return "Google App Engine Development/x.x.x"
			* if will run locally, and "Google App Engine/x.x.x" if run on production envorinment */
			
			if (serverInfo.contains("Development")) {
				// TODO  set profile to dev
			} else {
				// TODO set profile to production
			}
			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
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
