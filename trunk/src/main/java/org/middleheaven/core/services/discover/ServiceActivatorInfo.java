package org.middleheaven.core.services.discover;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Params;


public class ServiceActivatorInfo {
	
	public ServiceActivatorInfo(Class<ServiceActivator> activatorType) {
		super();
		this.activatorType = activatorType;
		
		for (Method m : activatorType.getMethods()){
			if (Modifier.isStatic(m.getModifiers())){
				continue;
			}
			
			if ( m.isAnnotationPresent(Params.class) ){
				// requires 

				ServiceInfo pinfo = new ServiceInfo(m.getParameterTypes()[0].getName(),m);
				Params p = m.getAnnotation(Params.class);
				String [] params = p.value();
				
				for (String s : params){
					String[] kv = s.split("=");
					pinfo.addParam(kv[0], kv[1]);
				}
				
				this.servicesRequired.add(pinfo);
				
			} else if (m.isAnnotationPresent(Publish.class)){
				// publish 
				ServiceInfo pinfo = new ServiceInfo(m.getReturnType().getName(),m);
				Publish p = m.getAnnotation(Publish.class);
				String [] params = p.value();
				
				for (String s : params){
					String[] kv = s.split("=");
					pinfo.addParam(kv[0], kv[1]);
				}
				
				servicesProvided.add(pinfo);
			}
		}
		
		if (this.servicesProvided.isEmpty()){
			throw new IllegalStateException("Activator " + activatorType.getName() + " does not provide any service.");
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
	
	
	public ServiceActivatorInfo requires(ServiceInfo serviceInfo){
		this.servicesRequired.add(serviceInfo);
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

	public void resolveRequiments(ServiceActivator activator,ServiceContext context) {
		
		for (ServiceInfo sinfo : this.servicesRequired){
			
			Method m = sinfo.getMethod();
			Object obj = context.getService(m.getParameterTypes()[0], sinfo.getParams());
			ReflectionUtils.invoke(Void.class, m, activator, obj);
		}
	
	}

	public void publishServices(ServiceActivator activator,ServiceContext context) {
	
		for (ServiceInfo sinfo : this.servicesProvided){
			
			Method m = sinfo.getMethod();

			Object implementation = ReflectionUtils.invoke(m.getReturnType(), m, activator);
			Class type = m.getReturnType();
			context.register(type,implementation, sinfo.getParams());
		}
	
	}

	public void unPublishServices(ServiceActivator activator,ServiceContext context) {
		
		for (ServiceInfo sinfo : this.servicesProvided){
			
			Method m = sinfo.getMethod();

			Object implementation = ReflectionUtils.invoke(m.getReturnType(), m, activator);
			Class type = m.getReturnType();
			context.unRegister(type,implementation, sinfo.getParams());
		}
	
	}

}
