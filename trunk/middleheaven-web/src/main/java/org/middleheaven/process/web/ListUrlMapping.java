/**
 * 
 */
package org.middleheaven.process.web;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class ListUrlMapping extends UrlMapping {

	private final List<UrlMapping> mappings = new LinkedList<UrlMapping>();
	
	private final boolean matchAll;

	public ListUrlMapping (){
		this(false);
	}
	
	public ListUrlMapping (boolean matchAll){
		this.matchAll = matchAll;
	}
	
	public ListUrlMapping add(UrlMapping other) {
		this.mappings.add(other);
		return this;
	}
	
	public ListUrlMapping remove(UrlMapping other) {
		this.mappings.remove(other);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(String url) {
		
		if (matchAll){
			boolean match = true;
			for (UrlMapping m : mappings){
				match = match && m.match(url);
			}
			return match;
		} else {
			for (UrlMapping m : mappings){
				if (m.match(url)){
					return true;
				} 
			}
			return false;
		}
	
		
		
	}

}
