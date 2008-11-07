package org.middleheaven.osgi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.cache.BundleCache;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.framework.util.StringMap;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Constants;

public class OSGiActivator extends ServiceActivator {

	private Felix felix = null;

	@Override
	public void activate(ServiceContext context) {

		// Create a case-insensitive configuration property map.
		Map<String,String> configMap = new StringMap(false);
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

		try {
			// Create host activator;
			List<BundleActivator> list = new ArrayList<BundleActivator>();
			//list.add(new ServiceListenerActivator());
			//list.add(new ContainerActivator(container));
			//list.add(new FileRepositoryActivator());
			//list.add(new LoggingActivator());
			
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

	@Override
	public void inactivate(ServiceContext context) {
		felix.stopAndWait();
	}

}
