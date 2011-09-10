package org.middleheaven.process.web;

import org.middleheaven.util.StringUtils;

/**
 * A {@link UrlMapping} determines if a Url matches a certain criteria. 
 *  
 *  TODO rename to UrlMatcher or UrlPattern
 */
public abstract class UrlMapping {

	public static UrlMapping notMatch(final UrlMapping mapping){
		return new UrlMapping(){

			@Override
			public boolean match(String url) {
				return !mapping.match(url);
			}
			
		};
	
	}

	public static UrlMapping matchSimplePattern(final String pattern){
		return new UrlMapping(){

			@Override
			public boolean match(String url) {
				return StringUtils.simplePatternMatch(pattern, url);
			}
			
		};
	}
	
	public static UrlMapping matchAll(){
		return new UrlMapping(){

			@Override
			public boolean match(String url) {
				return true;
			}
			
		};
	}

	public abstract boolean match (String url);
}
