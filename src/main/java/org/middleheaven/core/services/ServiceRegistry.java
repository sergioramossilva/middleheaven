package org.middleheaven.core.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.middleheaven.core.services.discover.ServiceDiscoveryEngine;

public final class ServiceRegistry {

	private ServiceRegistry(){}
	
    static ServiceContext context;
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
    
    protected static void removeAllEngines(){
    	for (ServiceDiscoveryEngine engine : engines){
    		engine.stop(context);
		}
    	engines.clear();
    }

    
    public static void addServiceListener (ServiceListener listener){
    	context.addServiceListener(listener);
    }
    
    public static void removeServiceListener (ServiceListener listener){
    	context.removeServiceListener(listener);
    }
    
    /**
     * Returns a service implementation compatible with <code>serviceClass</code>
     * @param <T> the serviceClass type
     * @param serviceClass the serviceClass contract
     * @return a service implementation compatible with <code>serviceClass</code> contract.
     * @throws ServiceNotFoundException if no implementation is found for the service
     */
    public static <T> T getService(Class<T> serviceClass){
    	return getService(serviceClass, Collections.<String,String>emptyMap());
    }
    
    /**
     * Returns a service implementation compatible with <code>serviceClass</code>
     * and the specified service properties
     * @param <T> the serviceClass type
     * @param serviceClass the serviceClass contract
     * @param properties the specific properties for the service implementation
     * @return a service implementation compatible with <code>serviceClass</code> contract.
     * @throws ServiceNotFoundException if no implementation is found for the service or the properties
     */
    public static <T> T getService(Class<T> serviceClass, Map<String,String> properties){
    	if (context == null){
    		// TODO throw exception when service context is null
    	}
    	T service = context.getService(serviceClass, properties);
    	if (service==null){
    		throw new ServiceNotFoundException(serviceClass.getName());
    	}
    	return service;
    }

    public static <T, I extends T> void register(Class<T> serviceClass,I implementation) {
		context.register(serviceClass, implementation, null);
	}
    
	public static <T, I extends T> void register(Class<T> serviceClass,I implementation, Map<String,String> properties) {
		context.register(serviceClass, implementation, properties);
	}

	public static void unRegister(Object implementation) {
		context.unRegister(implementation);
	}
	
	public static void unRegister(Class<?> serviceType) {
		context.unRegister(serviceType);
	}

}
