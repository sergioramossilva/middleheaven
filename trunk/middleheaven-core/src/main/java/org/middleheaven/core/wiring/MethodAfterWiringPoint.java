package org.middleheaven.core.wiring;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.ReflectionException;

/**
 * Used a {@link Method} as a {@link AfterWiringPoint}.
 */
public class MethodAfterWiringPoint implements AfterWiringPoint{

	private Method method;
	private WiringSpecification<?> methodSpecification;
	private WiringSpecification<?>[] paramsSpecifications;
	
//	public WiringSpecification<?> getMethodSpecification() {
//		return methodSpecification;
//	}
//
//	public WiringSpecification<?>[] getParamsSpecifications() {
//		return paramsSpecifications;
//	}

	/**
	 * 
	 * Constructor.
	 * @param method the method used to wire objects.
	 * @param methodSpecification the wiring specification of the method. 
	 * @param paramsSpecifications the wiring specification for each method parameter.
	 */
	public MethodAfterWiringPoint(Method method, WiringSpecification<?> methodSpecification, WiringSpecification<?>[] paramsSpecifications) {
		super();
		this.method = method;
		this.methodSpecification = methodSpecification;
		this.paramsSpecifications = paramsSpecifications;
	}


	@Override
	public String toString(){
		return method.getName();
	}
	
	@Override
	public int hashCode(){
		return method.hashCode();
	}
	
	@Override
	public boolean equals(Object other){
		return other instanceof MethodAfterWiringPoint && this.method.equals(((MethodAfterWiringPoint)other).method);
	}
	
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public <T> T writeAtPoint(EditableBinder binder, T object){
		if(object ==null){
			return null;
		}
		
		method.setAccessible(true);
		Object[] params = new Object[paramsSpecifications.length];
		for (int i=0;i<paramsSpecifications.length;i++){
			params[i] = binder.getInstance(paramsSpecifications[i]);
			if (params[i]==null && paramsSpecifications[i].isRequired()){
				throw new BindingException("Value to bind with parameter of index " + i +  
						" in method " + method.getDeclaringClass().getName() +"." + 
						method.getName()+ " was not found ");
			}
		}
	
		try {
			
			method.invoke(object, params);
		} catch (Exception e) {
			throw ReflectionException.manage(e, object.getClass());
		} 
		
		return object;
	}

	@Override
	public boolean isRequired() {
		for (WiringSpecification ws : paramsSpecifications){
			if (ws.isRequired()){
				return true;
			}
		}
		return false;
	}
}
