package org.middleheaven.core.wiring.activation;

import java.lang.reflect.Method;
import java.util.Collection;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.ConfigurationException;
import org.middleheaven.core.wiring.DefaultWiringModelParser;
import org.middleheaven.util.collections.ParamsMap;

public class AnnotationBasedDependencyResolver implements ActivatorDependencyResolver{

	@Override
	public void resolveDependency(Class<? extends Activator> activatorType,
			UnitActivatorDepedencyModel model) {
		
		new DefaultWiringModelParser().readWiringModel(activatorType, model);
		
		// find publish points
		
		Collection<Method> methods =  Introspector.of(activatorType).inspect().methods().annotatedWith(Publish.class).retriveAll();

		for (Method method : methods){
			if (method.getParameterTypes().length!=0){
				throw new ConfigurationException("@" + Publish.class.getSimpleName() + " cannot be used on a method with parameters");
			}
			
			Publish p = method.getAnnotation(Publish.class);
			String[] paramPairs = p.value();
			ParamsMap params = new ParamsMap();
			for (String paramPair : paramPairs){
				String[] values = paramPair.split("=");
				if(values.length!=2){
					throw new IllegalStateException("Param pair expected to be in format name=value bu found" + paramPair);
				}
				params.put(values[0], values[1]);
			}
			model.addPublishPoint(new MethodPublishPoint(method, params));
		}

		
	}

}
