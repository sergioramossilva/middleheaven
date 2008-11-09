package org.middleheaven.core.services.discover;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.middleheaven.core.dependency.InicializationNotPossibleException;
import org.middleheaven.core.dependency.InicializationNotResolvedException;
import org.middleheaven.core.dependency.Starter;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceNotFoundException;
import org.middleheaven.core.wiring.BindingException;
import org.middleheaven.core.wiring.WiringContext;

public class ServiceActivatorStarter implements Starter<ServiceActivatorInfo> {

	private ServiceContext context;
	private List<ServiceActivator> activators;

	public ServiceActivatorStarter(ServiceContext context, List<ServiceActivator> activators){
		this.context = context;
		this.activators = activators;
	}

	@Override
	public void inicialize(ServiceActivatorInfo info,
			WiringContext wiringContext)
	throws InicializationNotResolvedException,
	InicializationNotPossibleException {
		try{
			ServiceActivator activator = wiringContext.getInstance(info.getActivatorType());
			activators.add(activator);

			activator.activate(context);
		} catch (ServiceNotFoundException e){
			// service is not yet available
			throw new InicializationNotResolvedException();
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

