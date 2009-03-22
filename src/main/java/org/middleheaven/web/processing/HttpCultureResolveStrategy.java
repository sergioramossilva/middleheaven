package org.middleheaven.web.processing;

import javax.servlet.http.HttpServletRequest;

import org.middleheaven.global.Culture;

/**
 * Determines what is the culture associated with the HTTP request
 * 
 */
public interface HttpCultureResolveStrategy {

	public Culture resolveFrom(HttpServletRequest request);
	
}
