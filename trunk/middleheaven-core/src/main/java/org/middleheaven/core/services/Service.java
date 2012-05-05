/**
 * 
 */
package org.middleheaven.core.services;

import java.util.ArrayList;
import java.util.Collection;

import org.middleheaven.util.collections.ParamsMap;

/**
 * 
 */
public final class Service {


	private final ParamsMap params ;
	private final Class<?> contractInterface;
	private final ServiceActivator activator;

	private boolean activated = false;
	private Collection<ServiceDependency> dependencies = new ArrayList<ServiceDependency>(3);

	protected Service(Class<?> contractInterface, ServiceActivator activator, ParamsMap params){
		this.contractInterface = contractInterface;
		this.params = params;
		this.activator = activator;
	}

	public String getName(){
		return getServiceContract().getName();
	}

	public Class<?> getServiceContract(){
		return contractInterface;
	}

	public ParamsMap getParams(){
		return params;
	}

	public Collection<ServiceDependency> getDependencies(){
		return dependencies;
	}
	
	public void addDependency(ServiceDependency dependency){
		this.dependencies.add(dependency);
	}
	
	public String toString(){
		return getName();
	}

	public boolean isActivated(){
		return activated;
	}

	/**
	 * Activate de service. If the service is already activated an  exceptino will be thrown
	 * @param serviceContext
	 */
	public void activate(ServiceContext serviceContext){
		if (this.activated){
			throw new IllegalStateException("Service already activated");
		}

		try {
			this.activator.activate(serviceContext);
			this.activated = true;
		} catch (RuntimeException e){
			throw e;
		}
	}

	public int hashCode(){
		return contractInterface.getName().hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Service) && equalsService((Service)obj); 
	}


	private boolean equalsService(Service other) {
		return this.contractInterface.getName().equals(other.contractInterface) && this.params.containsSame(other.params);
	}
}
