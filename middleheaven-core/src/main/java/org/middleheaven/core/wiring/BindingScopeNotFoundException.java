/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public class BindingScopeNotFoundException extends BindingException {

	private static final long serialVersionUID = -1549161148069499581L;

	/**
	 * Constructor.
	 * @param cause
	 */
	public BindingScopeNotFoundException(String scopeName) {
		super("Scope " + scopeName + " is not registered");
	}

}
