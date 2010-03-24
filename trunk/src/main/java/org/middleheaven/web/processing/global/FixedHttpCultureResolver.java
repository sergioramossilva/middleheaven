package org.middleheaven.web.processing.global;

import org.middleheaven.global.Culture;
import org.middleheaven.web.processing.HttpContext;

public class FixedHttpCultureResolver implements
		HttpCultureResolver {

	public static FixedHttpCultureResolver instanceFor(Culture culture){
		return new FixedHttpCultureResolver(culture);
	}

	private Culture culture;
	
	private FixedHttpCultureResolver(Culture culture){
		this.culture = culture;
	}
	
	@Override
	public Culture resolveFrom(HttpContext context) {
		return culture;
	}

}
