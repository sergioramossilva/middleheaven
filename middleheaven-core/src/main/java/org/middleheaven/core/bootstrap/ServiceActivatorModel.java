/**
 * 
 */
package org.middleheaven.core.bootstrap;

import java.util.Collection;
import java.util.LinkedList;

import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;

/**
 * 
 */
public class ServiceActivatorModel {


	private ServiceActivator activator;
	Collection<ServiceSpecification> specs = new LinkedList<ServiceSpecification>();

	public ServiceActivatorModel(ServiceActivator activator){
		this.activator = activator;


		activator.collectRequiredServicesSpecifications(specs);


	}

	/**
	 * @return
	 */
	public Collection<ServiceSpecification> getRequiredServices() {
		return specs;
	}

	/**
	 * Called to activate units. 
	 */
	public  void activate(ServiceContext serviceContext){
		this.activator.activate(serviceContext);
	}

	/**
	 * Called to inactivity units. 
	 * The activator must release resources, deregister services and free dependencies 
	 */
	public  void inactivate(ServiceContext serviceContext){

	}


}
