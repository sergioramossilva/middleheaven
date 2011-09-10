package org.middleheaven.core.services;

import java.util.Collections;
import java.util.Map;

public class ServiceEvent {
	
	public enum ServiceEventType{
		ADDED,
		TEMPORARY_REMOVED,
		REMOVED
	}
	
	private Class<?> serviceClass;
	private ServiceEventType type;
	private Object implementation;
	private Map<String, String> params;
	

	public ServiceEvent(ServiceEventType type,Class<?> serviceClass, Object implementation ,Map<String, String> params ) {
		super();
		this.serviceClass = serviceClass;
		this.type = type;
		this.implementation= implementation;
		this.params = Collections.unmodifiableMap(params);
	}
	
	public Object getImplementation() {
		return implementation;
	}

	public Class<?> getServiceClass() {
		return serviceClass;
	}
	
	public ServiceEventType getType() {
		return type;
	}

	public Map<String, String> getParams() {
		return params;
	}
}
