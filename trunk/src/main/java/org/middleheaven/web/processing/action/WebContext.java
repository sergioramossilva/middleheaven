package org.middleheaven.web.processing.action;

import org.middleheaven.global.Culture;
import org.middleheaven.ui.AbstractContext;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.global.HttpCultureResolver;



public abstract class WebContext extends AbstractContext implements HttpContext{


	public static final String REQUEST_REMOTE_ADDRESS = "_REQUEST_REMOTE_ADDRESS";
	public static final String REQUEST_REMOTE_HOST = "_REQUEST_REMOTE_HOST";
	public static final String REQUEST_SERVER_PORT = "_REQUEST_SERVER_PORT";
	public static final String REQUEST_SERVER_NAME = "_REQUEST_SERVER_NAME";
	public static final String REQUEST_URL = "_REQUEST_URL";
	
	private HttpCultureResolver httpCultureResolver;

	public WebContext(HttpCultureResolver httpCultureResolver) {
		super();
		this.httpCultureResolver = httpCultureResolver;
	}
	
	@Override
	public Culture getCulture() {
		return httpCultureResolver.resolveFrom(this);
	}
	
}
