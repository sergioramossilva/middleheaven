/**
 * 
 */
package org.middleheaven.core.services;

import java.util.Collections;
import java.util.Map;

/**
 * 
 */
public class ServiceBuilder {

	

	public static ServiceBuilder forContract(Class<?> contractInterface){
		return new ServiceBuilder(contractInterface);
	}

	private Class<?> contractInterface;
	private ServiceActivator activator;
	private Map<String, Object> params = Collections.emptyMap();
	
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
	
	public ServiceBuilder withParams(Map<String, Object> params){
		this.params = params;
		return this;
	}
	
	public Service newInstance(){
		return new Service(contractInterface, activator, params);
	}
}
