package org.middleheaven.application;

import org.middleheaven.core.services.ServiceContext;


/**
 * The set of all modules of an application.
 * 
 */
public interface ApplicationContext {


	public ApplicationRegistry getRegistry();
	
	public ServiceContext getServiceContext();
}
