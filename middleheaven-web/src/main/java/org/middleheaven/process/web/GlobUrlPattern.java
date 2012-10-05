/**
 * 
 */
package org.middleheaven.process.web;

import org.middleheaven.util.StringUtils;

/**
 * Mathes a GLlob Pattern to the given url.
 * 
 * Glob pattens are expressed using * for any char match and ? for a single char match.
 */
public class GlobUrlPattern extends UrlPattern {

	
	private String pattern;

	public GlobUrlPattern (String pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double match(String url) {
		
		// match the glob pattern to the url.
		// measure how many letters match exactly
		
		if(StringUtils.simplePatternMatch(pattern, url)){
			
			double count = 0;
			for (int i =0; i < pattern.length(); i++){
				if (pattern.charAt(i) == '*'){
					count += 1.5d;
				} else if (pattern.charAt(i) == '?'){
					count += 0.5d;
				} else {
					count++;
				}
			}
			return count / url.length();
		}
		
		return 0d;
	}

	
	public boolean equals(Object other){
		return (other instanceof GlobUrlPattern) && ((GlobUrlPattern) other).pattern.equals(this.pattern);
	}
	
	public int hashCode(){
		return this.pattern.hashCode();
	}
	
	public String toString(){
		return pattern;
	}
}
