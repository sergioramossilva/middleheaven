package org.middleheaven.web.processing.global;

import java.util.List;

import org.middleheaven.global.Culture;
import org.middleheaven.web.processing.HttpContext;

/**
 * Obtains the request culture directly from the information in the request.
 */
public final class RequestAgentHttpCultureResolver implements HttpCultureResolver {

	private Culture defaultCulture;

	public RequestAgentHttpCultureResolver(Culture culture){
		this.defaultCulture = culture;
	}
	
	public RequestAgentHttpCultureResolver(){
		this(Culture.defaultValue());
	}
	
	@Override
	public Culture resolveFrom(HttpContext context) {
		// TODO compare with supported cultures an use culture with maximum prioriy that is also supported
		final List<Culture> cultures = context.getAgent().getBrowserInfo().getCultures();
		return  cultures.isEmpty() ?  defaultCulture : cultures.get(0);
	}

}
