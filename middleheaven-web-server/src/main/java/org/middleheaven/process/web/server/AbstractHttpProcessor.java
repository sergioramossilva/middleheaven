package org.middleheaven.process.web.server;

import org.middleheaven.process.web.HttpProcessException;



public abstract class AbstractHttpProcessor implements HttpProcessor {


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(HttpProcessorConfig config) {
		//no-op
	}
	
	@Override
	public Outcome process(HttpServerContext context) throws HttpProcessException {
		
		return doProcess(context);
	}
	
	public abstract Outcome doProcess(HttpServerContext context) throws HttpProcessException;




}
