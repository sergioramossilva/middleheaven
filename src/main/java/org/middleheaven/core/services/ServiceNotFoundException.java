package org.middleheaven.core.services;

/**
 * Indicates no implementation was found for the required service
 * 
 *
 */
public class ServiceNotFoundException extends ServiceException {

	public ServiceNotFoundException(String serviceName) {
		super(serviceName + " not found");
	}

}
