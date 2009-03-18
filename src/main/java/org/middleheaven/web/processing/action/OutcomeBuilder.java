package org.middleheaven.web.processing.action;

public interface OutcomeBuilder {

	ActionMappingBuilder forwardTo(String url);
	ActionMappingBuilder redirectTo(String url);
	ActionMappingBuilder redirectTo(int erroCode);
}
