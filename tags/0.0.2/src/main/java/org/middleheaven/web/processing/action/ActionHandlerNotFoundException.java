package org.middleheaven.web.processing.action;

public class ActionHandlerNotFoundException extends RuntimeException {

	public ActionHandlerNotFoundException(HttpMethod httpService,Class<?> controllerClass ,String actionNameFromURL) {
		super("No handler found for action " + actionNameFromURL + " or method " + httpService + " in presenter " + controllerClass);
	}

}
