package org.middleheaven.process;

/**
 * Scope for context attributes.
 */
public enum ContextScope {

	RENDERING,
	APPLICATION,
	SESSION,
	REQUEST,
	REQUEST_COOKIES,
	REQUEST_HEADERS,
	PARAMETERS, // read only
	CONFIGURATION // read only
	
	
}
