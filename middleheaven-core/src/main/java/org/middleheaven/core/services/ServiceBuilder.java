/**
 * 
 */
package org.middleheaven.core.services;

import java.util.Collections;
import java.util.Map;

/**
 * 
 */
public class ServiceBuilder<T> {

	

	public static <S> ServiceBuilder<S> forContract(Class<S> contractInterface){
		return new ServiceBuilder<S>(contractInterface);
	}

	private Class<?> contractInterface;
	private ServiceActivator activator;
	private Map<String, Object> params = Collections.emptyMap();
	
	/**
	 * Constructor.
	 * @param contractInterface
	 */
	private ServiceBuilder(Class<?> contractInterface) {
		this.contractInterface = contractInterface;
	}

	public ServiceBuilder<T> activatedBy(ServiceActivator activator){
		this.activator = activator;
		return this;
	}
	
	public ServiceBuilder<T> withParams(Map<String, Object> params){
		this.params = params;
		return this;
	}
	
//	public Service implementedBy(T implementation){
//		
//		activator = new ImplementationActivator(implementation, ServiceSpecification.forService(contractInterface, params));
//		return new Service(contractInterface, activator, params);
//	}
//	
	public Service newInstance(){
		return new Service(contractInterface, activator, params);
	}
}
