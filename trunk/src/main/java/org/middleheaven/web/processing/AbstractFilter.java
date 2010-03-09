package org.middleheaven.web.processing;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public abstract class AbstractFilter implements Filter{

	private FilterConfig filterConfig;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
	protected FilterConfig getFilterConfig(){
		return filterConfig;
	}
	
	@Override
	public void destroy() {
		this.filterConfig = null;
	}

}
