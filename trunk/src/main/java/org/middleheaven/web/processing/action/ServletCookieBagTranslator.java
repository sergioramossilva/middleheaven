package org.middleheaven.web.processing.action;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.web.processing.RequestCookie;

public class ServletCookieBagTranslator implements RequestCookieSource {



	private HttpServletResponse response;
	private HttpServletRequest request;

	public ServletCookieBagTranslator (HttpServletRequest request){
		this(request,null);
	}

	public ServletCookieBagTranslator (HttpServletResponse response){
		this(null,response);
	}

	public ServletCookieBagTranslator (HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}

	@Override
	public RequestCookieBag readAll() {
		RequestCookieBag bag = new RequestCookieBag();

		if (request !=null ){
			for (Cookie cookie : request.getCookies() ){

				RequestCookie rc = new RequestCookie(cookie.getName(), cookie.getValue());
				rc.setComment(cookie.getComment());
				rc.setDomain(cookie.getDomain());
				rc.setMaxAge(cookie.getMaxAge());
				rc.setPath(cookie.getPath());
				rc.setSecure(cookie.getSecure());
				rc.setVersion(cookie.getVersion());

				bag.add(rc);
			}
		}
		return bag;
	}

	@Override
	public void writeAll(RequestCookieBag bag) {
		if (response == null ){
			return;
		}
		for (RequestCookie cookie : bag ){
			write(cookie);
		}
	}

	@Override
	public void write(RequestCookie cookie) {
		if (response != null ){
			Cookie sc = new Cookie(cookie.getName(), cookie.getValue());
			sc.setComment(cookie.getComment());
			sc.setDomain(cookie.getDomain());
			sc.setMaxAge(cookie.getMaxAge());
			sc.setPath(cookie.getPath());
			sc.setSecure(cookie.isSecure());
			sc.setVersion(cookie.getVersion());
			
			response.addCookie(sc);
		}
	}

}
