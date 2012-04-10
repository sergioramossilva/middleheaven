/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * 
 */
public class WiringSpecificationNotSatisfiedException extends BindingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3053288791896375811L;


	/**
	 * Constructor.
	 * @param wiringSpecification
	 */
	public WiringSpecificationNotSatisfiedException(Class<?> type, WiringSpecification wiringSpecification) {
		super(createMessage(type, wiringSpecification));
	}


	/**
	 * @param type 
	 * @param wiringSpecification
	 * @return
	 */
	private static String createMessage(Class<?> type, WiringSpecification wiringSpecification) {
		return "Cannot satisfy dependency " + wiringSpecification.getContract() + " for class " + type;
	}


}
