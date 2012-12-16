package org.middleheaven.core.services;

public class ServiceNotAvailableException extends ServiceException {

	
	private static final long serialVersionUID = -2816013620542133281L;

	public ServiceNotAvailableException(String serviceName){
		super("Service " + serviceName + " is unavailable");
	}
}
