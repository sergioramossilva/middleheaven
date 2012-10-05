/**
 * 
 */
package org.middleheaven.ui.testui;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;

/**
 * 
 */
public class TestUIProxyHandler implements ProxyHandler {

	private TestUIComponent<?> original;

	/**
	 * Constructor.
	 * @param t
	 */
	public TestUIProxyHandler(TestUIComponent<?> original) {
		this.original = original;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(Object proxy, Object[] args, MethodDelegator delegator)
			throws Throwable {
		return delegator.invoke(original, args);  // execute the original method.
	}

}
