package org.middleheaven.core.services;

import java.util.Map;


/**
 * Primary interface to retrive and register services from {@link ServiceActivator}s.
 * 
 * 
 */
public interface ServiceContext {

	public  <T,I extends T> void register(Class<T> serviceClass, I implementation , Map<String, ? extends Object> properties );
	public  <T,I extends T> void register(Class<T> serviceClass, I implementation );
	
	public  <T,I extends T> void unRegister(Class<T> serviceClass, I implementation ,Map<String, ? extends Object> properties );
	public  <T,I extends T> void unRegister(Class<T> serviceClass, I implementation );

	public void unRegister(Object implementation);
	public void unRegister(Class<?> serviceType);

	public <T> T getService (Class<T> serviceClass,Map<String, ? extends Object> properties);
	public <T> T getService (Class<T> serviceClass);

	

}
