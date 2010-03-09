package org.middleheaven.web.processing;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GZipFilter  extends AbstractFilter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			String acceptEncoding = httpRequest.getHeader("accept-encoding");
			if (acceptEncoding != null && acceptEncoding.contains("gzip")) {

				GZipResponseWrapper wrappedResponse = new GZipResponseWrapper(httpResponse);

				chain.doFilter(request, wrappedResponse);
				wrappedResponse.finishResponse();

				return;
			} 
		}
		chain.doFilter(request, response);

	}

}
