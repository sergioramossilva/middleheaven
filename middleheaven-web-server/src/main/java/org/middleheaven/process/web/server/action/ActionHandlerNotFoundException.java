package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.HttpMethod;

public class ActionHandlerNotFoundException extends RuntimeException {

	public ActionHandlerNotFoundException(HttpMethod httpService,Class<?> controllerClass ,String actionNameFromURL) {
		super("No handler found for action " + actionNameFromURL + " or method " + httpService + " in presenter " + controllerClass);
	}

}
