package org.middleheaven.core.services.discover;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.wiring.Wire;


public class ServiceActivatorInfo {
	
	public ServiceActivatorInfo(Class<ServiceActivator> activatorType) {
		super();
		this.activatorType = activatorType;
		
		List<Constructor<ServiceActivator>> constructors =  ReflectionUtils.allAnnotatedConstructors( activatorType, Wire.class);

		if (constructors.isEmpty()){
			// search not annotated constructors
			constructors = ReflectionUtils.constructors(activatorType);
		} 
		
		for (Constructor<ServiceActivator> c : constructors){
			for (Class<?> ct : c.getParameterTypes()){
				this.requires(ct.getName());
			}
		}
		
	}

	private Class<ServiceActivator> activatorType;
	private List<ServiceInfo> servicesProvided = new LinkedList<ServiceInfo>();
	private List<ServiceInfo> servicesRequired = new LinkedList<ServiceInfo>();
	
	
	public String toString(){
		return this.activatorType.getName();
	}
	
	public Class<ServiceActivator> getActivatorType(){
		return this.activatorType;
	}

	public List<ServiceInfo> getServicesProvided() {
		return servicesProvided;
	}
	
	public List<ServiceInfo> getServicesRequired() {
		return servicesRequired;
	}
	
	public ServiceActivatorInfo provides(ServiceInfo serviceInfo){
		this.servicesProvided.add(serviceInfo);
		return this;
	}
	
	public ServiceActivatorInfo provides(String serviceName){
		this.servicesProvided.add(new ServiceInfo(serviceName));
		return this;
	}
	
	public ServiceActivatorInfo requires(ServiceInfo serviceInfo){
		this.servicesRequired.add(serviceInfo);
		return this;
	}
	
	public ServiceActivatorInfo requires(String serviceName){
		this.servicesRequired.add(new ServiceInfo(serviceName));
		return this;
	}

	public boolean depends(ServiceActivatorInfo other) {
		for (ServiceInfo si : this.servicesRequired){
			if (other.getServicesProvided().contains(si)){
				return true;
			}
		}
		return false;
	}


}
