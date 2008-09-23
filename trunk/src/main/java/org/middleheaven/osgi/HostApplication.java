package org.middleheaven.osgi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.cache.BundleCache;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.framework.util.StringMap;
import org.middleheaven.core.ContainerActivator;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

public class HostApplication{

	private ContainerActivator activator = null;
	private Felix felix = null;



	public HostApplication() {

	}

	public void init(){

		// Create a case-insensitive configuration property map.
		Map configMap = new StringMap(false);
		// Configure the Felix instance to be embedded.
		configMap.put(FelixConstants.EMBEDDED_EXECUTION_PROP, "true");
		// Add core OSGi packages to be exported from the class path
		// via the system bundle.
		configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES,
				"org.osgi.framework; version=1.3.0," +
				"org.osgi.service.packageadmin; version=1.2.0," +
				"org.osgi.service.startlevel; version=1.0.0," +
		"org.osgi.service.url; version=1.0.0");
		// Explicitly specify the directory to use for caching bundles.
		configMap.put(BundleCache.CACHE_PROFILE_DIR_PROP, "cache");

		try
		{
			// Create host activator;
			activator = new ContainerActivator(null);
			List list = new ArrayList();
			list.add(activator);

			// Now create an instance of the framework with
			// our configuration properties and activator.
			felix = new Felix(configMap, list);

			// Now start Felix instance.
			felix.start();


		}catch (Exception ex){
			System.err.println("Could not create framework: " + ex);
			ex.printStackTrace();
		}
	}


	public void shutdownApplication(){
		// Shut down the felix framework when stopping the
		// host application.
		//felix.shutdown();
	}
}
