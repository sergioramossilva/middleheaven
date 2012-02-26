package org.middleheaven.core.wiring.activation;

import org.middleheaven.core.wiring.BeanModel;

public interface ActivatorDependencyResolver {

	public void resolveDependency(Class<? extends Activator> activatorType, BeanModel model);
	
}
