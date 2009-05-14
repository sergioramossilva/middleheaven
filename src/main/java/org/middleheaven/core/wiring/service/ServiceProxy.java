package org.middleheaven.core.wiring.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.middleheaven.core.services.ServiceEvent;
import org.middleheaven.core.services.ServiceListener;
import org.middleheaven.core.services.ServiceUnavailableException;
import org.middleheaven.util.Hash;
import org.middleheaven.util.collections.CollectionUtils;

public class ServiceProxy<T> implements ServiceListener, InvocationHandler {

	Class<T> serviceClass;
	private Map<String, String> params;
	BlockingQueue<T> queue;

	public ServiceProxy(Class<T> serviceClass,Map<String,String> params){
		this(serviceClass,null,null,params);
	}
	
	protected ServiceProxy(Class<T> serviceClass, Object lateBinderObject, Method lateBinder, Map<String,String> params){
		this.serviceClass = serviceClass;
		this.params = params;
	}

	@Override
	public void onEvent(ServiceEvent event) {
	
		
		if (event.getServiceClass().equals(serviceClass) && CollectionUtils.equalContents(this.params, event.getParams())){
			if ( ServiceEvent.ServiceEventType.ADDED.equals(event.getType())){
				queue = new ArrayBlockingQueue<T>(1);
				queue.offer(serviceClass.cast(event.getImplementation()));
			} else if (ServiceEvent.ServiceEventType.TEMPORARY_REMOVED.equals(event.getType())){
				queue.clear();
			} else if (ServiceEvent.ServiceEventType.REMOVED.equals(event.getType())){
				queue.clear();
				queue = null;
			}
		}
	}

	@Override
	public Object invoke(Object obj, Method method, Object[] params)throws Throwable {
		if (queue==null){
			throw new ServiceUnavailableException(serviceClass.getName());
		}
		
		T implementation = queue.peek();
		if (queue.peek()==null){
			implementation = queue.take();
		}
	
		return method.invoke(implementation, params);
	}
	
	public boolean equals(Object other){
		return other instanceof ServiceProxy && 
		this.serviceClass.getName().equals(((ServiceProxy)other).serviceClass.getName()) && 
		CollectionUtils.equalContents(this.params, ((ServiceProxy)other).params);
	}
	
	public int hashCode(){
		return Hash.hash(this.serviceClass.getName()).hash(this.params).hashCode();
	}
}
