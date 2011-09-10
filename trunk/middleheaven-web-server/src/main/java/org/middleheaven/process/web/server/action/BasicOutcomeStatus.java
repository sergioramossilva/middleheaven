package org.middleheaven.process.web.server.action;

public enum BasicOutcomeStatus implements OutcomeStatus{

	SUCCESS,// all ok
	FAILURE, // exception thrown
	ERROR,// error thrown
	INVALID ,// validation failed
	TERMINATE,// presenter write the response. do not forward
	REDIRECT, // redirect
	NOT_FOUND; // page not found
	
}
