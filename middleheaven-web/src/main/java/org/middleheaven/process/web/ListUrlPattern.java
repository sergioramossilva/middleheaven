/**
 * 
 */
package org.middleheaven.process.web;

import java.util.LinkedList;
import java.util.List;

/**
 * A composition of {@link UrlPattern}s.
 * 
 * The matching accours in the order of insertion of the individual {@link UrlPattern} objects.
 * if the {@code matchAll} flag is ative all the patterns must match the input in some degreed.
 * If the {@code matchAll} flag is not ative, the maximum of the match will be returned.
 */
public class ListUrlPattern extends UrlPattern {

	private final List<UrlPattern> mappings = new LinkedList<UrlPattern>();
	
	private final boolean matchAll;

	public ListUrlPattern (){
		this(false);
	}
	
	public ListUrlPattern (boolean matchAll){
		this.matchAll = matchAll;
	}
	
	public ListUrlPattern add(UrlPattern other) {
		this.mappings.add(other);
		return this;
	}
	
	public ListUrlPattern remove(UrlPattern other) {
		this.mappings.remove(other);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double match(String url) {
		
		if (matchAll){
			// all need to match
			double match = 1;
			for (UrlPattern m : mappings){
				match = Math.min(match, m.match(url));
			}
			return match;
		} else {
			double max = 0;
			for (UrlPattern m : mappings){
				max = Math.max(max, m.match(url));
			}
			return max;
		}
	
		
		
	}

}
