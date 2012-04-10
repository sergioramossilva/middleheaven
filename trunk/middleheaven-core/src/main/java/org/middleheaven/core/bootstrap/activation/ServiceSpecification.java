/**
 * 
 */
package org.middleheaven.core.bootstrap.activation;

import java.util.Collections;
import java.util.Map;

/**
 * 
 */
public final class ServiceSpecification {

	private Class<?> serviceContractType;
	private Map<String, Object> parameters;
	private boolean optional;

	public ServiceSpecification(Class<?> serviceContractType){
		this(serviceContractType, Collections.<String,Object>emptyMap(), false);
	}
	
	public ServiceSpecification(Class<?> serviceContractType, boolean isOptional){
		this(serviceContractType, Collections.<String,Object>emptyMap(), isOptional);
	}
	
	public ServiceSpecification(Class<?> serviceContractType, Map<String, Object> parameters){
		this(serviceContractType, parameters, false);
	}
		
	public ServiceSpecification(Class<?> serviceContractType, Map<String, Object> parameters, boolean isOptional){
		this.serviceContractType = serviceContractType;
		this.parameters = parameters;
		this.optional = isOptional;
	}

	/**
	 * Obtains {@link Class<?>}.
	 * @return the serviceContractType
	 */
	public Class<?> getServiceContractType() {
		return serviceContractType;
	}

	/**
	 * Obtains {@link Map<String,Object>}.
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * Obtains {@link boolean}.
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}
	
	
	
}
