package org.middleheaven.process.web.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FrontEndServlet extends HttpServlet {

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

		try{

			ServletHttpServerService serverService = (ServletHttpServerService) this.getServletContext().getAttribute("httpService");
		
			if (serverService == null){
				throw new ServletException("HTTPServerService not found in contexts");
			}
			
			serverService.processRequest(request, response);
			

		} catch (ClassCastException e){
			// this servlet can only work with this specific implementation of HTTPServerService
			throw new ServletException("HTTPServerService not compatible with generic Servlet Container");
		} catch (ServletException e){
			throw e; 
		} catch (IOException e) {
			throw e;
		} catch (Throwable t){
			this.getServletContext().log("Unexpected Exception", t);
		}
		
		
		
		
		
	}
}
