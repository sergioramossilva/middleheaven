/**
 * 
 */
package org.middleheaven.core.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.util.CollectionUtils;

public final class RegistryServiceContext implements ServiceContext{

	final static Set<ServiceListener> serviceListeners = new CopyOnWriteArraySet<ServiceListener>();
	final static Map<String , List<ServiceBinding>> registry = new TreeMap<String , List<ServiceBinding>>();
	
	private void fireServiceAdded(Class<?> serviceClass){
		ServiceEvent event = new ServiceEvent(ServiceEvent.ServiceEventType.ADDED, serviceClass);
		for (ServiceListener s : serviceListeners){
			s.onEvent(event);
		}
	}
	
	private void fireServiceRemoved(Class<?> serviceClass){
		ServiceEvent event = new ServiceEvent(ServiceEvent.ServiceEventType.REMOVED, serviceClass);
		for (ServiceListener s : serviceListeners){
			s.onEvent(event);
		}
	}
	
	
	@Override
	public <T, I extends T> void register(Class<T> serviceClass, I implementation, Map<String,String> properties) {
		ServiceBinding b = new ServiceBinding(properties,implementation);
		List<ServiceBinding> list = registry.get(serviceClass.getName());
		if (list==null ){
			list = new CopyOnWriteArrayList();
			registry.put(serviceClass.getName(), list);
		}
		try{
			list.add(b);
			fireServiceAdded(serviceClass);
		} catch (RuntimeException e){
			list.remove(b);
			throw e;
		}
		
	}

	@Override
	public <T, I extends T> void unRegister(Class<T> serviceClass,I implementation, Map<String,String> properties) {
		ServiceBinding b = new ServiceBinding(properties,implementation);
		List<ServiceBinding> list = registry.get(serviceClass.getName());
		if (list!=null && list.remove(b) ){
			this.fireServiceRemoved(serviceClass);
		}
	}

	@Override
	public <T> T getService(Class<T> serviceClass, Map<String,String> properties) {
		List<ServiceBinding> list = registry.get(serviceClass.getName());
		if (list!=null && !list.isEmpty()){
			// no matching is necessary
			if (properties==null || properties.isEmpty()){
				return serviceClass.cast(list.get(0).implementation);
			}
			
			// rank and match
			double rank = 0;
			ServiceBinding selected = null;
			for (ServiceBinding b : list){
				if (b.match(properties)>rank){
					selected = b;
				}
			}
			
			if (selected !=null){
				return serviceClass.cast(selected.implementation);
			}
		}
		throw new ServiceNotFoundException(serviceClass.getName());
	}
	
	@Override
	public void addServiceListener(ServiceListener s) {
		serviceListeners.add(s);
	}

	@Override
	public void removeServiceListener(ServiceListener s) {
		serviceListeners.remove(s);
	}
	

	private static class ServiceBinding<I> {
		
		Map<String,String> properties;
		I implementation;
		
		public ServiceBinding(Map<String,String> properties, I implementation) {
			super();
			if (properties==null || properties.isEmpty()){
				this.properties = Collections.emptyMap();
			} else {
				this.properties = new TreeMap<String,String>(properties);
			}
			this.implementation = implementation;
		}
		
		public boolean equals(Object other){
			return other instanceof ServiceBinding && equals((ServiceBinding)other);
		}
		
		public int hashCode(){
			return this.implementation.hashCode();
		}
		
		public boolean equals(ServiceBinding other){
			return this.implementation == other.implementation && CollectionUtils.equals(properties, other.properties);
		}
	
		/**
		 * 0 = Does not match at all
		 * 1 = Match exactly
		 * @param properties
		 * @return
		 */
		public double match (Map<String,String> properties){
			if (this.properties.size() < properties.size()){
				return 0d;
			}
			
			int count=0;
			for (Map.Entry<String,String> entry : properties.entrySet() ){
				if (this.properties.get(entry.getValue()).equals(entry.getValue())){
					count++;
				}
			}
			
			return count * 1d / properties.size();
		}
	}





	
}