/**
 * 
 */
package org.middleheaven.core.services;

import org.middleheaven.util.collections.ParamsMap;

/**
 * 
 */
public class ServiceBuilder {

	

	public static ServiceBuilder forContract(Class<?> contractInterface){
		return new ServiceBuilder(contractInterface);
	}


	private Class<?> contractInterface;
	private ServiceActivator activator;
	private ParamsMap params;
	
	/**
	 * Constructor.
	 * @param contractInterface
	 */
	public ServiceBuilder(Class<?> contractInterface) {
		this.contractInterface = contractInterface;
	}

	public ServiceBuilder activatedBy(ServiceActivator activator){
		this.activator = activator;
		return this;
	}
	
	public ServiceBuilder withParams(ParamsMap params){
		this.params = params;
		return this;
	}
	
	public Service newInstance(){
		return new Service(contractInterface, activator, params);
	}
}
