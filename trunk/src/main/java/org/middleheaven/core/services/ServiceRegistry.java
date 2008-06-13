package org.middleheaven.core.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ServiceRegistry {

	private ServiceRegistry(){}
	
    private static ServiceContext context = new RegistryServiceContext();
	private static Set<ServiceDiscoveryEngine> engines = new HashSet<ServiceDiscoveryEngine>();
	

    protected static void addEngine(ServiceDiscoveryEngine engine){
    	engines.add(engine);
    	engine.init(context);
    }
    
    protected static void removeEngine(ServiceDiscoveryEngine engine){
    	if (engines.contains(engine)){
			engine.stop(context);
			engines.remove(engine);
		}
    }

    
    public static void addServiceListener (ServiceListener listener){
    	context.addServiceListener(listener);
    }
    
    public static void removeServiceListener (ServiceListener listener){
    	context.removeServiceListener(listener);
    }
    
    public static <T> T getService(Class<T> serviceClass){
    	return getService(serviceClass, Collections.<String,String>emptyMap());
    }
    
    public static <T> T getService(Class<T> serviceClass, Map<String,String> properties){
    	return context.getService(serviceClass, properties);
    }

    public static <T, I extends T> void register(Class<T> serviceClass,I implementation) {
		context.register(serviceClass, implementation, null);
	}
    
	public static <T, I extends T> void register(Class<T> serviceClass,I implementation, Map<String,String> properties) {
		context.register(serviceClass, implementation, properties);
	}
}
