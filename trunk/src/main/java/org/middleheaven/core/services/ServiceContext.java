package org.middleheaven.core.services;

import java.util.Map;

import org.middleheaven.logging.LogBook;

public interface ServiceContext extends ServiceAtivatorContext{

	public  <T,I extends T> void register(Class<T> serviceClass, I implementation , Map<String,String> properties );
	public  <T,I extends T> void unRegister(Class<T> serviceClass, I implementation ,Map<String,String> properties );

	
	public <T> T getService (Class<T> serviceClass,Map<String,String> properties);
	public LogBook getLogBook();
	

}
