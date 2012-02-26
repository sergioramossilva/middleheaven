package org.middleheaven.core.wiring.activation;

import org.middleheaven.core.wiring.BeanModel;
import org.middleheaven.core.wiring.DefaultWiringModelParser;

public class AnnotationBasedDependencyResolver implements ActivatorDependencyResolver{

	@Override
	public void resolveDependency(Class<? extends Activator> activatorType,BeanModel model) {
		
		new DefaultWiringModelParser().readBeanModel(activatorType, model);
		

		
	}

}
