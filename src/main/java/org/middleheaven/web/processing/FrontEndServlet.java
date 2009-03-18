package org.middleheaven.web.processing;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.core.services.ServiceRegistry;

public final class FrontEndServlet extends HttpServlet {

	private static final long serialVersionUID = -6229608965293497721L;

	public final void doPut(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(request,response);
	}

	public final void doDelete(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(request,response);
	}

	public final void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(request,response);
	}

	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		doService(request,response);
	}

	private void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		HttpProcessor processor = ServiceRegistry.getService(HTTPServerService.class).processorFor(request.getRequestURI());
		
		if (processor == null){
			response.sendError(HTTPErrors.NOT_FOUND.errorCode()); 
			return;
		}
		
		processor.process(request, response);
		
	}
}
