package org.middleheaven.web.processing.global;

import org.middleheaven.global.Culture;
import org.middleheaven.web.processing.HttpContext;

/**
 * Determines what is the culture associated with the HTTP request
 * 
 */
public interface HttpCultureResolver {

	public Culture resolveFrom(HttpContext context);
	
}
