/**
 * 
 */
package org.middleheaven.process.web;

import org.middleheaven.util.StringUtils;

/**
 * 
 */
public class PatternUrlMapping extends UrlMapping {

	
	private String pattern;

	public PatternUrlMapping (String pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(String url) {
		return StringUtils.simplePatternMatch(pattern, url);
	}

	
	public boolean equals(Object other){
		return (other instanceof PatternUrlMapping) && ((PatternUrlMapping) other).pattern.equals(this.pattern);
	}
	
	public int hashCode(){
		return this.pattern.hashCode();
	}
	
	public String toString(){
		return pattern;
	}
}
