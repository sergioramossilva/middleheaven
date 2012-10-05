/**
 * 
 */
package org.middleheaven.core.reflection;

/**
 * 
 */
public class ReadOnlyPropertyException extends RuntimeException {

	private static final long serialVersionUID = 639760013466050512L;

	/**
	 * Constructor.
	 * @param name
	 */
	public ReadOnlyPropertyException(String name) {
		super("Property " + name + " is read only");
	}


}
