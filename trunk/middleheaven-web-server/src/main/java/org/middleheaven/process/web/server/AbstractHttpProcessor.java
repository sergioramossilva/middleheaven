package org.middleheaven.process.web.server;

import org.middleheaven.process.web.HttpProcessException;



public abstract class AbstractHttpProcessor implements HttpProcessor {


	@Override
	public Outcome process(HttpServerContext context) throws HttpProcessException {
		
		return doProcess(context);
	}
	
	public abstract Outcome doProcess(HttpServerContext context) throws HttpProcessException;




}
