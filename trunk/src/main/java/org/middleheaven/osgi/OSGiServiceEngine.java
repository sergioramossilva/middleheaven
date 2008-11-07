package org.middleheaven.osgi;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceDiscoveryEngine;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class OSGiServiceEngine implements ServiceDiscoveryEngine{

	private BundleContext bundleContext;
	private ServiceContext serviceContext;
	private ServiceOSGiListener listener  = new ServiceOSGiListener();
	
    OSGiServiceEngine( BundleContext context){
		this.bundleContext =  context;

	}
     
	@Override
	public void init(ServiceContext context) {
		this.serviceContext = context;
		this.bundleContext.addServiceListener(listener);
	}
	
	@Override
	public void stop(ServiceContext context) {
		this.bundleContext.removeServiceListener(listener);
	}
	
	private class ServiceOSGiListener implements org.osgi.framework.ServiceListener{

		@Override
		public void serviceChanged(org.osgi.framework.ServiceEvent event) {
			ServiceReference reference = event.getServiceReference();
			Map<String,String> properties;
			String[] keys = reference.getPropertyKeys();
			if (keys.length==0){
				properties = Collections.emptyMap();
			} else {
				properties = new TreeMap<String,String>();
				for (String k : keys){
					properties.put(k , String.valueOf(reference.getProperty(k)));
				}
			}
			
			Object service = bundleContext.getService(reference);
			if (event.getType()==1){
				serviceContext.register((Class<Object>)service.getClass(),service.getClass().cast(service), properties);
			} else {
				serviceContext.unRegister((Class<Object>)service.getClass(), service, properties);
			}
		
		} 
	}






}
