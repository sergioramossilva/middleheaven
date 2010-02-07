package org.middleheaven.web.processing;

import org.middleheaven.web.processing.action.BasicOutcomeStatus;
import org.middleheaven.web.processing.action.OutcomeStatus;

public class Outcome {

	OutcomeStatus status;
	private boolean doRedirect = false;
	boolean isError;
	private String url;
	private HttpCode httpCode = HttpCode.OK;
	
	public Outcome(OutcomeStatus status, String url) {
		this(status,url,false,HttpCode.OK);
	}
	
	public Outcome(OutcomeStatus status, String url, boolean doRedirect, HttpCode redirectCode) {
		super();
		this.status = status;
		this.doRedirect = doRedirect;
		this.url = url;
		this.isError = false;
		this.httpCode = redirectCode;
	}
	
	public Outcome(OutcomeStatus status, HttpCode error) {
		super();
		this.status = status;
		this.doRedirect = true;
		this.isError = true;
		this.httpCode = error;
	}

	protected void setRedirect(boolean redirect){
		this.doRedirect = redirect;
	}
	
	protected void setHttpCode(HttpCode code) {
		this.httpCode = code;
	}
	
	public HttpCode getHttpCode(){
		return httpCode;
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
	
	public String getParameterizedURL(){
		return this.getUrl();
	}
}
