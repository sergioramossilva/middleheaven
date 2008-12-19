/**
 * 
 */
package org.middleheaven.core.reflection;

import java.lang.reflect.Method;

public class OriginalMethodHandler implements ProxyHandler {
	
	public Object invoke(Object self, Method m, Method original,Object[] args) throws Throwable {
		return original.invoke(self, args);  // execute the original method.
	}
}