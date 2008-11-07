package org.middleheaven.core.services.engine;

import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.services.discover.ServiceActivatorDiscoveryEngine;

public class ActivatorBagServiceDiscoveryEngine extends ServiceActivatorDiscoveryEngine {

	private List<Class<ServiceActivator>> activators = new LinkedList<Class<ServiceActivator>>();
	
	public ActivatorBagServiceDiscoveryEngine(){}

	public ActivatorBagServiceDiscoveryEngine addActivator(Class<? extends ServiceActivator> activator){
		activators.add((Class<ServiceActivator>) activator);
		return this;
	}

	@Override
	protected List<Class<ServiceActivator>> discoverActivators(ServiceContext context) {
		return activators;
	}
	





}
