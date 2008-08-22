package org.middleheaven.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.io.repository.upload.UploadManagedFileRepository;

public class WebFacadeServlet extends HttpServlet {


	public void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
		doService(request,response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		doService(request,response);
	}
	
	private void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		UploadManagedFileRepository rep = new UploadManagedFileRepository(request);
		
		WebContext context = new WebContext(request,response);
		context.setAttribute(ContextScope.REQUEST, "uploads", rep);
		
		// resolve mapped WebCommand from url

		WebCommandMappingService mapper = ServiceRegistry.getService(WebCommandMappingService.class);

		WebCommandMapping webCommandMapping = mapper.resolve(request.getRequestURL());

		List<Interceptor> interceptors = webCommandMapping.interceptors();

		for (Interceptor interceptor : interceptors){
			interceptor.interceptForward(context);
		}

		Outcome outcome = webCommandMapping.execute(context);

		for (Interceptor interceptor : interceptors){
			interceptor.interceptReverse(context);
		}

		if (outcome.isError){
			response.sendError(Integer.parseInt(outcome.url));
		}else if (outcome.isDoRedirect()){
			response.sendRedirect(outcome.url);
		} else {
			request.getRequestDispatcher(outcome.url).forward(request, response);
		}
		
	}
}
