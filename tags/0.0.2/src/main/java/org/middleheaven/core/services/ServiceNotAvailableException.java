package org.middleheaven.core.services;

public class ServiceNotAvailableException extends ServiceException {

	
	public ServiceNotAvailableException(String serviceName){
		super("Service " + serviceName + " is unavailable");
	}
}
