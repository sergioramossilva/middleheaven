package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.HttpStatusCode;


public interface OutcomeBuilder {

	ActionMappingBuilder forwardTo(String url);
	ActionMappingBuilder forwardTo(String url, String asContentType);
	
	ActionMappingBuilder redirectTo(String url);
	ActionMappingBuilder redirectTo(String url, RedirectOptions options);
	ActionMappingBuilder redirectTo(HttpStatusCode error);

	/**
	 * Foward to referer
	 * @param lastAction the action to perform in the referer page
	 * @return the builder object.
	 */
	ActionMappingBuilder forwardToLast(String lastAction);
	
	/**
	 * Redirect to referer
	 * @return the builder object.
	 */
	ActionMappingBuilder redirectToLast();
	


}
