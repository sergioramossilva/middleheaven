package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionException;

public final class ConstructorWiringPoint {

	private Constructor constructor;
	
	public ConstructorWiringPoint(Constructor constructor){
		if (Modifier.isPrivate(constructor.getModifiers())){
			throw new IllegalArgumentException("Constructor is private");
		}
		this.constructor = constructor;
	}
	
	
	public <T> T construct(EditableBinder binder){
		constructor.setAccessible(true);

		// determine wiringQuery for the contructor params
		Annotation[][] constructorAnnnnotations = constructor.getParameterAnnotations();
		Class<?>[] types = constructor.getParameterTypes();
		Set[] specs = new Set[types.length];
		Object[] objects = new Object[types.length];

		for (int p =0; p< constructorAnnnnotations.length;p++){
			// inner classes have a added parameter on index 0 that 
			// get annotations does not cover.
			// read from end to start

			int typeIndex = types.length - 1 - p;
			int annotIndex = constructorAnnnnotations.length - 1 -p;
			
			specs[typeIndex] = new HashSet();


			for (Annotation a : constructorAnnnnotations[annotIndex]){

				if (a.annotationType().isAnnotationPresent(BindingSpecification.class) ||
						a.annotationType().isAnnotationPresent(ScopeSpecification.class)){
					specs[typeIndex].add(a);
				}
			}

			objects[typeIndex] = binder.getInstance(WiringSpecification.search(types[typeIndex],specs[typeIndex]));
		}

		try {
			@SuppressWarnings("unchecked") T newInstance = (T) constructor.newInstance(objects);
			return newInstance;
		} catch (Exception e) {
			throw ReflectionException.manage(e,constructor.getDeclaringClass());
		}
	}
}
