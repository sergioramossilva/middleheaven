package org.middleheaven.core.services;


/**
 * A strategy pattern common interface for loading services implementations.
 * 
 *
 */
public interface ServiceDiscoveryEngine {


	public void init(ServiceContext context);

	public void stop(ServiceContext context);


}
