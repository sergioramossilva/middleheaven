package org.middleheaven.web.processing.action;

public class ActionHandlerNotFoundException extends RuntimeException {

	public ActionHandlerNotFoundException(HttpMethod httpService,
			Class<?> controllerClass) {
		super("No handler found for method " + httpService + " in presenter " + controllerClass);
	}

}
