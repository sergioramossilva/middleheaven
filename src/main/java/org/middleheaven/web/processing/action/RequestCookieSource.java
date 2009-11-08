package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.RequestCookie;


public interface RequestCookieSource {

	public RequestCookieBag readAll();
	public void writeAll(RequestCookieBag bag);
	public void write(RequestCookie cookie);
}
