package org.middleheaven.web.processing;



public abstract class AbstractHttpProcessor implements HttpProcessor {


	@Override
	public Outcome process(HttpContext context) throws HttpProcessException {
		
		return doProcess(context);
	}
	
	public abstract Outcome doProcess(HttpContext context) throws HttpProcessException;




}
