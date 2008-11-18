package org.middleheaven.core.wiring.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.services.ServiceEvent;
import org.middleheaven.core.services.ServiceListener;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.ServiceUnavailableException;

public class ServiceProxy<T> implements ServiceListener, InvocationHandler {

	Class<T> serviceClass;
	T service;
	Method lateBinder;
	private Object lateBinderObject;
	
	public static <T> T newInstance(Class<T> type,Object lateBinderObject, Method lateBinder){
		return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, new ServiceProxy<T>(type,lateBinderObject,lateBinder));
	}
	
	public ServiceProxy(Class<T> serviceClass){
		this(serviceClass,null,null);
	}
	
	protected ServiceProxy(Class<T> serviceClass, Object lateBinderObject, Method lateBinder){
		this.serviceClass = serviceClass;
		this.lateBinder = lateBinder;
		this.lateBinderObject = lateBinderObject;
		
		ServiceRegistry.addServiceListener(this);

	}

	@Override
	public void onEvent(ServiceEvent event) {
		
		// TODO read service params 
		if (event.getServiceClass().equals(serviceClass)){
			if (event.getType() == ServiceEvent.ServiceEventType.ADDED){
				service = ServiceRegistry.getService(serviceClass);
				if (this.lateBinder!=null &&  this.lateBinderObject!=null){
					ReflectionUtils.invoke(Void.class, this.lateBinder, this.lateBinderObject, service);
				}
			} else if (event.getType() == ServiceEvent.ServiceEventType.REMOVED){
				service = null;
			}
		}
	}

	@Override
	public Object invoke(Object obj, Method method, Object[] params)throws Throwable {
		if (service==null){
			throw new ServiceUnavailableException(serviceClass.getName());
		}
		return method.invoke(service, params);
	}
}
