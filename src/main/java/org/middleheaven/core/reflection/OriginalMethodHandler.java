/**
 * 
 */
package org.middleheaven.core.reflection;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;

public class OriginalMethodHandler implements MethodHandler {
	public Object invoke(Object self, Method m, Method original,Object[] args) throws Throwable {
		return original.invoke(self, args);  // execute the original method.
	}
}