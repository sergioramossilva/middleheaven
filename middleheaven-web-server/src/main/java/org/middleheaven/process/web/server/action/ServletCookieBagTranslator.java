package org.middleheaven.process.web.server.action;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.process.web.HttpCookieBag;
import org.middleheaven.process.web.HttpCookieReader;
import org.middleheaven.process.web.HttpCookieWriter;
import org.middleheaven.quantity.time.Period;
import org.middleheaven.util.StringUtils;

public class ServletCookieBagTranslator implements HttpCookieReader, HttpCookieWriter {



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

	private Period maxAgeToPeriod(int seconds){
		return seconds < 0 ? null : Period.seconds(seconds);
	}
	
	private int maxAgeToSeconds(Period seconds){
		return seconds == null ? -1 : (int)seconds.seconds();
	}
	
	@Override
	public HttpCookieBag readAll() {
		HttpCookieBag bag = new HttpCookieBag();

		if (request !=null && request.getCookies() != null && request.getCookies().length > 0){
		
			for (Cookie cookie :  request.getCookies()){

				HttpCookie rc = new HttpCookie(cookie.getName(), cookie.getValue());
				rc.setComment(cookie.getComment());
				rc.setDomain(cookie.getDomain());
				rc.setMaxAge(maxAgeToPeriod(cookie.getMaxAge()));
				rc.setPath(cookie.getPath());
				rc.setSecure(cookie.getSecure());
				rc.setVersion(cookie.getVersion());

				bag.add(rc);
			}
		}
		return bag;
	}

	@Override
	public void writeAll(HttpCookieBag bag) {
		if (response == null ){
			return;
		}
		for (HttpCookie cookie : bag ){
			write(cookie);
		}
	}

	@Override
	public void write(HttpCookie cookie) {
		if (response != null ){
			Cookie sc = new Cookie(cookie.getName(), cookie.getValue());
			sc.setComment(cookie.getComment());
			sc.setMaxAge(maxAgeToSeconds(cookie.getMaxAge()));
			sc.setSecure(cookie.isSecure());
			sc.setVersion(cookie.getVersion());
			
//			sc.setDomain(cookie.getDomain());
			sc.setPath(StringUtils.ensureStartsWith(cookie.getPath(), "/"));
			
			response.addCookie(sc);
		}
	}

	
	public int getSize(){
		return this.request.getCookies().length;
	}
}
