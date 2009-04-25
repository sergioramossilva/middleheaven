package org.middleheaven.core.wiring.activation;

public interface ActivatorDependencyResolver {

	
	public void resolveDependency(Class<? extends UnitActivator> activatorType, UnitActivatorDepedencyModel model);
}
