package org.middleheaven.web.processing;

public interface HttpFilter {

	
	public void doFilter(HttpContext context, HttpFilterChain chain);
}
