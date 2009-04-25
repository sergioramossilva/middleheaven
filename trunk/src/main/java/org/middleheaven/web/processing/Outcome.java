package org.middleheaven.web.processing;

import org.middleheaven.web.processing.action.BasicOutcomeStatus;
import org.middleheaven.web.processing.action.OutcomeStatus;

public class Outcome {

	OutcomeStatus status;
	boolean doRedirect = false;
	boolean isError;
	private String url;
	
	public Outcome(OutcomeStatus status, boolean doRedirect, String url) {
		super();
		this.status = status;
		this.doRedirect = doRedirect;
		this.url = url;
		this.isError = false;
	}
	
	public Outcome(OutcomeStatus status, int error) {
		super();
		this.status = status;
		this.doRedirect = true;
		this.url = Integer.toString(error);
		this.isError = true;
	}
	
	public OutcomeStatus getStatus() {
		return status;
	}
	public boolean isDoRedirect() {
		return doRedirect;
	}
	
	public String getUrl() {
		return url;
	}

	public boolean isError() {
		return isError;
	}

	public boolean isTerminal() {
		return BasicOutcomeStatus.TERMINATE.equals(status);
	}
	
	public String toString(){
		return status.toString() + (this.isDoRedirect() ? "redirectTo" : "fowardTo") + url;
	}
}
