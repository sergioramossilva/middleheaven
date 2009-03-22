package org.middleheaven.web.processing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.global.Culture;

public abstract class AbstractHttpProcessor implements HttpProcessor {

	private HttpCultureResolveStrategy strategy;
	private Culture culture;
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws HttpProcessException {
		culture = strategy.resolveFrom(request);
		doProcess(request,response);
	}
	
	@Override
	public Culture getCulture() {
		return culture;
	}

	
	public abstract void doProcess(HttpServletRequest request, HttpServletResponse response) throws HttpProcessException;


	@Override
	public void setCultureResolveStrategy(HttpCultureResolveStrategy strategy) {
		this.strategy = strategy;
	}

}
