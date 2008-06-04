package org.middleheaven.core.services;

public class ServiceUnavailableException extends ServiceException {

	
	public ServiceUnavailableException(String serviceName){
		super(serviceName + " is unavailable");
	}
}
