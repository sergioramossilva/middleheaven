package org.middleheaven.core.services.discover;

import java.lang.reflect.Method;

import org.middleheaven.util.ParamsMap;

public class ServiceInfo {

	private String serviceInterfaceTypeName;
	private ParamsMap params = new ParamsMap();
	private Method method;
	
	public ServiceInfo(String serviceInterfaceTypeName, Method method) {
		this.serviceInterfaceTypeName = serviceInterfaceTypeName;
		this.method = method;
	}

	protected Method getMethod(){
		return method;
	}
	
	public ParamsMap getParams() {
		return params;
	}

	public ServiceInfo addParam(String name, String value){
		this.params.put(name, value);
		return this;
	}
	
	public String getServiceInterfaceTypeName() {
		return serviceInterfaceTypeName;
	}
	
	public boolean equals(Object other){
		return other instanceof ServiceInfo && equals((ServiceInfo)other);
	}

	public boolean equals(ServiceInfo other){
		return this.serviceInterfaceTypeName.equals(other.serviceInterfaceTypeName);
	}
	
	public int hashCode(){
		return this.serviceInterfaceTypeName.hashCode();
	}
	
	
}
