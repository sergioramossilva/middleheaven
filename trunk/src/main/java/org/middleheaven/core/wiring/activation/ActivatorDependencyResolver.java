package org.middleheaven.core.wiring.activation;

public interface ActivatorDependencyResolver {

	
	public void resolveDependency(Class<? extends Activator> activatorType, UnitActivatorDepedencyModel model);
}
