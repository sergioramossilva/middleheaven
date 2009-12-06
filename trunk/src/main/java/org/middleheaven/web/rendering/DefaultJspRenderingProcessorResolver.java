package org.middleheaven.web.rendering;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.web.processing.HttpProcessException;
import org.middleheaven.web.processing.Outcome;
import org.middleheaven.web.processing.action.HttpProcessIOException;
import org.middleheaven.web.processing.action.HttpProcessServletException;
import org.middleheaven.web.processing.action.RequestResponseWebContext;

public class DefaultJspRenderingProcessorResolver implements RenderingProcessorResolver {

	DefaultJspRendingProcessor processor = new DefaultJspRendingProcessor();
	private String path;
	
	public DefaultJspRenderingProcessorResolver(){
		this("/WEB-INF/view");
	}
	
	public DefaultJspRenderingProcessorResolver(String path){
		this.path = path;
	}
	
	@Override
	public RenderingProcessor resolve(String url) {
		return processor;
	}

	
	public class DefaultJspRendingProcessor implements RenderingProcessor{


		public DefaultJspRendingProcessor(){
			
		}
		
		private String realPath(String url){
			url = url.substring(0, url.lastIndexOf('.'));
			return path + "/" + url + ".jsp";
		}
		
		@Override
		public void process(RequestResponseWebContext context, Outcome outcome)
		throws HttpProcessException {


			HttpServletResponse response = context.getResponse();
			HttpServletRequest request = context.getRequest();

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
