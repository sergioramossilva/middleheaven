package org.middleheaven.web.rendering;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.HttpProcessIOException;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.action.HttpProcessServletException;
import org.middleheaven.process.web.server.action.RequestResponseWebContext;

public class DefaultJspRenderingProcessorResolver extends AbstractJspProcessorResolver {

	DefaultJspRendingProcessor processor = new DefaultJspRendingProcessor();
	private String path;
	
	public DefaultJspRenderingProcessorResolver(){
		this("/WEB-INF/view");
	}
	
	public DefaultJspRenderingProcessorResolver(String path){
		this.path = path;
	}
	
	@Override
	public RenderingProcessor resolve(String url, String contentType) {
		return processor;
	}
	
	@Override
	public boolean canProcess(String url, String contentType) {
		return true;
	}
	
	public class DefaultJspRendingProcessor implements RenderingProcessor{


		public DefaultJspRendingProcessor(){
			
		}
		
		private String realPath(String url){
			return path + "/" +stripExtention(url) + ".jsp";
		}
		
		private String stripExtention (String name){
			int pos = name.lastIndexOf('.');
			 
			if (pos == 0) {
				return "";
			} else if (pos > 0){
				return name.substring(0, pos);
			} else {
				return name;
			}
			
		}
		
		@Override
		public void process(RequestResponseWebContext context, Outcome outcome,String contentType)
		throws HttpProcessException {

			injectBrowserClient(context);
			
			HttpServletResponse response = context.getServletResponse();
			HttpServletRequest request = context.getServletRequest();
	
		

			try{
				request.getRequestDispatcher(realPath(outcome.getUrl())).include(request, response);
				
			} catch (IOException e){
				throw new HttpProcessIOException(e);
			} catch (ServletException e) {
				throw new HttpProcessServletException(e);
			}
		}



	}





}
