package org.middleheaven.process;


/**
 * Scope for context attributes.
 */
public enum ContextScope {

	/**
	 * Attributes present in this context are available to the intire application
	 * and prevail as long as the JVM is running.
	 */
	APPLICATION,
	
	RENDERING,
	
	SESSION,
	CONFIGURATION, // read only
	
	/**
	 * Read only information sent with the request.
	 */
	REQUEST_PARAMETERS,
	
	REQUEST,
	REQUEST_COOKIES,
	REQUEST_HEADERS
	;
}
