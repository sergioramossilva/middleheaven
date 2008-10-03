package org.middleheaven.web;

public enum ContextScope {

	APPLICATION,
	SESSION,
	REQUEST,
	PARAMETERS, // read only
	CONFIGURATION // read only
}
