package org.middleheaven.core.services;

public class ServiceEvent {
	
	public enum ServiceEventType{
		ADDED,
		REMOVED
	}
	
	Class<?> serviceClass;
	ServiceEventType type;
	
	
	public ServiceEvent(ServiceEventType type,Class<?> serviceClass) {
		super();
		this.serviceClass = serviceClass;
		this.type = type;
	}
	
	public Class<?> getServiceClass() {
		return serviceClass;
	}
	
	public ServiceEventType getType() {
		return type;
	}
}
