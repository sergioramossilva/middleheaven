package org.middleheaven.core.wiring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Mergable;

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

	public ConstructorWiringPoint merge(ConstructorWiringPoint other) {
		if (other ==null){
			return this;
		}
		if (!this.constructor.equals(other.constructor)){
			throw new IllegalStateException("ConstructorWiringPoint with diferent constructors cannot be merged ");
		} 
		
		WiringSpecification newMethodSpecification = this.methodSpecification;
		if (methodSpecification !=null){
			newMethodSpecification = newMethodSpecification.merge(other.methodSpecification);
		}
		
		Mergable[] a = 	this.paramsSpecifications;
		Mergable[] b = 	other.paramsSpecifications;
		
		Mergable[] c  = CollectionUtils.merge(a,b);
		

		return new ConstructorWiringPoint (this.constructor, newMethodSpecification, (WiringSpecification<?>[]) c);
		
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
			if (params[i]==null){ // constructor params are always mandatory even if configured otherwise 
				throw new BindingException("Value to bind with parameter of index " + i +  
						" in constructor " + constructor.getDeclaringClass().getName() +"." + 
						constructor.getName()+ " was not found ");
			}
		}

		try {
			@SuppressWarnings("unchecked") T newInstance = (T) constructor.newInstance(params);
			return newInstance;
		} catch (Exception e) {
			throw ReflectionException.manage(e,constructor.getDeclaringClass());
		}
	}


	
}
