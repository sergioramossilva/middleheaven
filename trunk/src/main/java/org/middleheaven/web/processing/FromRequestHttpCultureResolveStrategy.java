package org.middleheaven.web.processing;

import org.middleheaven.global.Culture;

/**
 * Obtains the request culture directly from the information in the request.
 */
public final class FromRequestHttpCultureResolveStrategy 
	implements HttpCultureResolveStrategy {

	@Override
	public Culture resolveFrom(HttpContext context) {
		return  context.getCulture();
	}

}
