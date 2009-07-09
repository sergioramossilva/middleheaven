package org.middleheaven.web.processing;

import org.middleheaven.global.Culture;

public abstract class AbstractHttpProcessor implements ControlProcessor {

	private HttpCultureResolveStrategy strategy = new FromRequestHttpCultureResolveStrategy();
	private Culture culture;
	
	@Override
	public Outcome process(HttpContext context) throws HttpProcessException {
		culture = strategy.resolveFrom(context);
		return doProcess(context);
	}
	
	@Override
	public Culture getCulture() {
		return culture;
	}

	
	public abstract Outcome doProcess(HttpContext context) throws HttpProcessException;


	@Override
	public void setCultureResolveStrategy(HttpCultureResolveStrategy strategy) {
		this.strategy = strategy;
	}

}
