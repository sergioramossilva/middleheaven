/**
 * 
 */
package org.middleheaven.process.web;

/**
 * 
 */
public class ViewNotFoundException extends HttpProcessException {


	private static final long serialVersionUID = 6469014471816559201L;

	/**
	 * Constructor.
	 * @param cause
	 */
	public ViewNotFoundException(String viewName) {
		super("View " + viewName + " has not found");
	}

}
