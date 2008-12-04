package org.middleheaven.core.services.discover;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.middleheaven.core.dependency.InicializationNotPossibleException;
import org.middleheaven.core.dependency.InicializationNotResolvedException;
import org.middleheaven.core.dependency.Starter;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceNotFoundException;
import org.middleheaven.core.wiring.service.ServiceProxy;

public class ServiceActivatorStarter implements Starter<ServiceActivatorInfo> {

	private ServiceContext context;
	private List<ServiceActivator> activators;

	public ServiceActivatorStarter(ServiceContext context,  List<ServiceActivator> activators){
		this.context = context;
		this.activators = activators;
	}

	@Override
	public void inicialize(ServiceActivatorInfo info) throws InicializationNotResolvedException,InicializationNotPossibleException {

		try{
			ServiceActivator activator = ReflectionUtils.newInstance(info.getActivatorType());

			// fill requirements 
			info.resolveRequiments(activator,context);
			// activate
			activator.activate(context);
			// publish services 
			info.publishServices(activator,context);

			// add activator to context for future inactivation
			activators.add(activator);

		} catch (ServiceNotFoundException e){
			// service is not yet available
			throw new InicializationNotResolvedException();
		}catch (RuntimeException e){
			e.printStackTrace();
			throw new InicializationNotPossibleException();
		}
	}

	@Override
	public void inicializeWithProxy(ServiceActivatorInfo info) throws InicializationNotResolvedException,InicializationNotPossibleException {
		try{
			ServiceActivator activator = ReflectionUtils.newInstance(info.getActivatorType());
			// fill requirements with proxy
			for (ServiceInfo sinfo : info.getServicesRequired()){

				Method m = sinfo.getMethod();
				Object obj;
				try{
					 obj = context.getService(m.getParameterTypes()[0], sinfo.getParams());
				} catch (ServiceNotFoundException e){
					 obj =  ServiceProxy.newInstance(m.getParameterTypes()[0],activator,m,sinfo.getParams());
				}
				ReflectionUtils.invoke(Void.class, m, activator, obj);
			}
			
			// activate
			activator.activate(context);
			// publish services 
			info.publishServices(activator,context);

			// add activator to context for future inactivation
			activators.add(activator);
			
		}catch (RuntimeException e){
			throw new InicializationNotPossibleException();
		}
	}


	@Override
	public List<ServiceActivatorInfo> sort(List<ServiceActivatorInfo> dependencies) {

		Collections.sort(dependencies , new Comparator<ServiceActivatorInfo>(){

			@Override
			public int compare(ServiceActivatorInfo a,
					ServiceActivatorInfo b) {

				int diff = a.getServicesRequired().size() - b.getServicesRequired().size();
				if (diff==0){
					if (a.depends (b)){
						return -1;
					} else if (b.depends(a)){
						return 1;
					}
					return 0;
				} else {
					return -diff;
				}
			}

		});

		return dependencies;
	}


}

