package org.middleheaven.core.wiring;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.ReflectionException;

public class MethodWiringPoint implements AfterWiringPoint{

	private Method method;
	private WiringSpecification<?> methodSpecification;
	private WiringSpecification<?>[] paramsSpecifications;
	
	public WiringSpecification<?> getMethodSpecification() {
		return methodSpecification;
	}

	public WiringSpecification<?>[] getParamsSpecifications() {
		return paramsSpecifications;
	}

	public int hashCode(){
		return method.hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof MethodWiringPoint && this.method.equals(((MethodWiringPoint)other).method);
	}
	
	public MethodWiringPoint(Method method,WiringSpecification<?> methodSpecification,WiringSpecification<?>[] paramsSpecifications) {
		super();
		this.method = method;
		this.methodSpecification = methodSpecification;
		this.paramsSpecifications = paramsSpecifications;
	}

	public <T> T writeAtPoint(EditableBinder binder, T object){
		if(object ==null){
			return null;
		}
		
		method.setAccessible(true);
		Object[] params = new Object[paramsSpecifications.length];
		for (int i=0;i<paramsSpecifications.length;i++){
			params[i] = binder.getInstance(paramsSpecifications[i]);
		}
	
		try {
			method.invoke(object, params);
		} catch (Exception e) {
			ReflectionException.manage(e, object.getClass());
		} 
		
		return object;
	}
}
