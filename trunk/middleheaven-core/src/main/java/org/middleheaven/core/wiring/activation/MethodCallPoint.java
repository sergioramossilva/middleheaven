/**
 * 
 */
package org.middleheaven.core.wiring.activation;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.ReflectionException;

/**
 * 
 */
public class MethodCallPoint implements PostCreatePoint, PreDestroiPoint {

	
	private Method method;
	
	public MethodCallPoint (Method method){
		this.method = method;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(Object target) {
		try {
			this.method.invoke(target, new Object[0]);
		} catch (Exception e) {
			throw ReflectionException.manage(e, method.getDeclaringClass());
		} 
	}

}
