package org.middleheaven.web.processing;

import javax.servlet.http.HttpServletRequest;

import org.middleheaven.global.Culture;

/**
 * Obtains the request culture directly from the information in the request.
 */
public final class FromRequestHttpCultureResolveStrategy 
	implements HttpCultureResolveStrategy {

	@Override
	public Culture resolveFrom(HttpServletRequest request) {
		return Culture.valueOf(request.getLocale());
	}

}
