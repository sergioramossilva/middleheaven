package org.middleheaven.core.services;

import java.util.Map;

import org.middleheaven.logging.LogBook;

public interface ServiceContext extends ServiceAtivatorContext{

	public  <T,I extends T> void register(Class<T> serviceClass, I implementation , Map<String,Object> properties );
	public  <T,I extends T> void unRegister(Class<T> serviceClass, I implementation ,Map<String,Object> properties );

	public void unRegister(Object implementation);
	public void unRegister(Class<?> serviceType);

	public <T> T getService (Class<T> serviceClass,Map<String, Object> properties);
	public LogBook getLogBook();

	

}
