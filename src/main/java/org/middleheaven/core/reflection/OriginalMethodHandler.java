/**
 * 
 */
package org.middleheaven.core.reflection;


public class OriginalMethodHandler implements ProxyHandler {

	@Override
	public Object invoke(Object self, Object[] args, MethodDelegator delegator)throws Throwable {
		return delegator.invokeSuper(self, args);  // execute the original method.
	}
}