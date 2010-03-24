package org.middleheaven.web.processing.global;

import org.middleheaven.global.Culture;
import org.middleheaven.web.processing.HttpContext;

/**
 * Obtains the request culture directly from the information in the request.
 */
public final class RequestAgentHttpCultureResolver implements HttpCultureResolver {

	public RequestAgentHttpCultureResolver(){}
	
	@Override
	public Culture resolveFrom(HttpContext context) {
		return  context.getAgent().getBrowserInfo().getCulture();
	}

}
