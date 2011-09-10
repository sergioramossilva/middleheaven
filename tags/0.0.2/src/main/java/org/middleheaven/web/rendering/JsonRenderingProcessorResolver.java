package org.middleheaven.web.rendering;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.global.text.JsonFormatter;
import org.middleheaven.web.processing.HttpProcessException;
import org.middleheaven.web.processing.Outcome;
import org.middleheaven.web.processing.action.HttpProcessIOException;
import org.middleheaven.web.processing.action.RequestResponseWebContext;


/**
 * Writes to the response any object in request attributes under the name "jsonObject".
 */
public class JsonRenderingProcessorResolver implements RenderingProcessorResolver{

	private static JsonRenderingProcessor processor = new JsonRenderingProcessor();
	
	@Override
	public boolean canProcess(String url, String contentType) {
		return url.endsWith("json") || contentType.equalsIgnoreCase("application/json") || contentType.equalsIgnoreCase("text/json");
	}

	@Override
	public RenderingProcessor resolve(String url, String contentType) {
		return processor;
	}
	
	
	static class JsonRenderingProcessor implements RenderingProcessor {

	
		@Override
		public void process(RequestResponseWebContext context, Outcome outcome,String contentType) throws HttpProcessException {
			HttpServletResponse response = context.getResponse();
			HttpServletRequest request = context.getRequest();

			try{
				
				response.setContentType(outcome.getContentType());

				Object obj =request.getAttribute("jsonObject");
				
				
				JsonFormatter<Object> formatter = JsonFormatter.formatOnly();
				
				response.getWriter().print(formatter.format(obj));
				
			} catch (IOException e){
				throw new HttpProcessIOException(e);
			} catch (Exception e){
				throw new HttpProcessException(e);
			}
		}
		
	}




}
