package org.middleheaven.core.services.discover;

import java.util.List;

import org.middleheaven.core.dependency.ClassDependableProperties;
import org.middleheaven.core.dependency.InicializationNotPossibleException;
import org.middleheaven.core.dependency.InicializationNotResolvedException;
import org.middleheaven.core.dependency.Starter;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.WiringContext;

public class ServiceActivatorStarter<S extends ServiceActivator> implements Starter<Class<S>, ClassDependableProperties<S>> {

	private ServiceContext context;
	private List<ServiceActivator> activators;
	
	public ServiceActivatorStarter(ServiceContext context, List<ServiceActivator> activators){
		this.context = context;
		this.activators = activators;
	}

	@Override
	public ClassDependableProperties<S> wrap(Class<S> object) {
		return new ClassDependableProperties<S>(object);
	}

	@Override
	public void inicialize(ClassDependableProperties<S> dependableProperties,
			WiringContext wiringContext) throws InicializationNotResolvedException,
			InicializationNotPossibleException {

		ServiceActivator activator = wiringContext.getInstance(dependableProperties.getType());
		activators.add(activator);
		activator.activate(context);
		
	}
}

