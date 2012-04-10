/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public class CanSatisfyDependencyException extends BindingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1538675213080711024L;

	/**
	 * Constructor.
	 * @param contract
	 */
	public CanSatisfyDependencyException(Class contract, WiringTarget target) {
		super("Can not inicialize " + target.getDeclaringType() + ".Binding not found for " + contract);
	}

}
