package org.middleheaven.core.services.discover;

import java.util.Map;
import java.util.TreeMap;

public class ServiceInfo {

	private String serviceInterfaceTypeName;
	private Map<String,String> params = new TreeMap<String,String>();
	
	public ServiceInfo(String serviceInterfaceTypeName) {
		this.serviceInterfaceTypeName = serviceInterfaceTypeName;
	}

	public Map<String, String> getParams() {
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
