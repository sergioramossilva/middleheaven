package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.wiring.annotations.BindingSpecification;
import org.middleheaven.core.wiring.annotations.ScopeSpecification;

public final class ConstructorWiringPoint {

	private Constructor<?> constructor;
	private WiringSpecification<?> methodSpecification;
	private WiringSpecification<?>[] paramsSpecifications;
	
	public WiringSpecification<?> getMethodSpecification() {
		return methodSpecification;
	}


	public WiringSpecification<?>[] getParamsSpecifications() {
		return paramsSpecifications;
	}


	public ConstructorWiringPoint(Constructor<?> constructor,WiringSpecification<?> methodSpecification,WiringSpecification<?>[] paramsSpecifications){
		if (Modifier.isPrivate(constructor.getModifiers())){
			throw new IllegalArgumentException("Constructor is private");
		}
		this.constructor = constructor;
		this.methodSpecification = methodSpecification;
		this.paramsSpecifications = paramsSpecifications;
	}
	
	
	public <T> T construct(EditableBinder binder){
		constructor.setAccessible(true);

		Object[] params = new Object[paramsSpecifications.length];
		for (int i=0;i<paramsSpecifications.length;i++){
			params[i] = binder.getInstance(paramsSpecifications[i]);
		}

		try {
			@SuppressWarnings("unchecked") T newInstance = (T) constructor.newInstance(params);
			return newInstance;
		} catch (Exception e) {
			throw ReflectionException.manage(e,constructor.getDeclaringClass());
		}
	}
}
