package org.middleheaven.process.web.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Servlet that handles all traffic related with MiddleHeaven.
 */
public class FrontEndServlet extends HttpServlet {

	private static final long serialVersionUID = -6229608965293497721L;

	/**
	 * 
	 * {@inheritDoc}
	 */
	public final void doPut(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(request,response);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public final void doDelete(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(request,response);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public final void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(request,response);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
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
			throw new ServletException("HTTPServerService not compatible with generic Servlet Container", e);
		} catch (Exception t){
			this.getServletContext().log("Unexpected Exception", t);
		} catch (Error t){
			this.getServletContext().log("Unexpected Error", t);
		}
		
		
		
		
		
	}
}
