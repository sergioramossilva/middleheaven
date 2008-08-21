package org.middleheaven.web;

public interface OutcomeBuilder {

	PresenterCommandMappingBuilder forwardTo(String url);
	PresenterCommandMappingBuilder redirectTo(String url);
	PresenterCommandMappingBuilder redirectTo(int erroCode);
}
