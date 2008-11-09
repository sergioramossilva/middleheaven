package org.middleheaven.core.services.engine;

import java.util.LinkedList;
import java.util.List;

import javassist.bytecode.Descriptor.Iterator;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.services.discover.ServiceActivatorDiscoveryEngine;
import org.middleheaven.core.services.discover.ServiceActivatorInfo;
import org.middleheaven.core.wiring.Wire;

public class ActivatorBagServiceDiscoveryEngine extends ServiceActivatorDiscoveryEngine {

	private List<ServiceActivatorInfo> activators = new LinkedList<ServiceActivatorInfo>();
	
	public ActivatorBagServiceDiscoveryEngine(){}

	public ActivatorBagServiceDiscoveryEngine addActivatorInfo(ServiceActivatorInfo info){
		activators.add(info);
		return this;
	}
	
	public ActivatorBagServiceDiscoveryEngine addActivator(Class<? extends ServiceActivator> type){
		ServiceActivatorInfo info = new ServiceActivatorInfo((Class<ServiceActivator>) type);

		activators.add(info);
		return this;
	}

	@Override
	protected List<ServiceActivatorInfo> discoverActivators(ServiceContext context) {
		return activators;
	}
	





}
