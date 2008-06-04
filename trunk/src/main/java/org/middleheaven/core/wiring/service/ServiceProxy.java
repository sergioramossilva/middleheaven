package org.middleheaven.core.wiring.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.middleheaven.core.services.ServiceEvent;
import org.middleheaven.core.services.ServiceListener;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.ServiceUnavailableException;

public class ServiceProxy<T> implements ServiceListener, InvocationHandler {

	Class<T> serviceClass;
	T service;
	
	public ServiceProxy(Class<T> serviceClass){
		this.serviceClass = serviceClass;
		ServiceRegistry.addServiceListener(this);
		service = ServiceRegistry.getService(serviceClass);
	}

	@Override
	public void onEvent(ServiceEvent event) {
		
		// TODO read serviceparams 
		if (event.getServiceClass().equals(serviceClass)){
			if (event.getType() == ServiceEvent.ServiceEventType.ADDED){
				service = ServiceRegistry.getService(serviceClass);
			} else if (event.getType() == ServiceEvent.ServiceEventType.REMOVED){
				service = null;
			}
		}
	}

	@Override
	public Object invoke(Object obj, Method method, Object[] params)throws Throwable {
		if (service==null){
			throw new ServiceUnavailableException("Service " + serviceClass.getName() + " is not present");
		}
		return method.invoke(service, params);
	}
}
