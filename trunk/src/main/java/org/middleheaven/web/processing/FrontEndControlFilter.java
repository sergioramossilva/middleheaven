package org.middleheaven.web.processing;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontEndControlFilter implements Filter{

	private FilterConfig filterConfig;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
	
		try {
			HttpServletRequest hRequest = (HttpServletRequest) request;
			HttpServletResponse hResponse = (HttpServletResponse) request;
			try{

				ServletHttpServerService serverService = (ServletHttpServerService) this.filterConfig.getServletContext().getAttribute("httpService");
			
				if (serverService == null){
					throw new ServletException("HTTPServerService not found in contexts");
				}
				
				serverService.processRequest(hRequest, hResponse);
				

			} catch (ClassCastException e){
				// this servlet can only work with this specific implementation of HTTPServerService
				throw new ServletException("HTTPServerService not compatible with generic Servlet Container");
			} catch (ServletException e){
				throw e; 
			} catch (IOException e) {
				throw e;
			} catch (Throwable t){
				this.filterConfig.getServletContext().log("Unexpected Exception", t);
				t.printStackTrace();
			}
			
		} catch (ClassCastException e){
			this.filterConfig.getServletContext().log("Cannot process non HTTP request/response", e);
			chain.doFilter(request, response);
		}
		
		
		
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

}
