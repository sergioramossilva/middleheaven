/**
 * 
 */
package org.middleheaven.core.reflection.inspection;

import org.middleheaven.core.reflection.ReflectionException;

/**
 * 
 */
public interface MethodDelegate {

	public Object invokeStatic(Object ... args) throws ReflectionException;
	public Object invoke(Object target, Object ... args) throws ReflectionException;
}
