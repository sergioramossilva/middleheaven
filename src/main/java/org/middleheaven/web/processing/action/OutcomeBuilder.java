package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpCode;


public interface OutcomeBuilder {

	ActionMappingBuilder forwardTo(String url);
	ActionMappingBuilder redirectTo(String url);
	ActionMappingBuilder redirectTo(String url, RedirectOptions options);
	ActionMappingBuilder redirectTo(HttpCode error);
}
