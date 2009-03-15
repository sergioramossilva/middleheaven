package org.middleheaven.web;

public interface OutcomeBuilder {

	ActionMappingBuilder forwardTo(String url);
	ActionMappingBuilder redirectTo(String url);
	ActionMappingBuilder redirectTo(int erroCode);
}
