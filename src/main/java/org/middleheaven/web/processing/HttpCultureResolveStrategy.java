package org.middleheaven.web.processing;

import org.middleheaven.global.Culture;

/**
 * Determines what is the culture associated with the HTTP request
 * 
 */
public interface HttpCultureResolveStrategy {

	public Culture resolveFrom(HttpContext context);
	
}
