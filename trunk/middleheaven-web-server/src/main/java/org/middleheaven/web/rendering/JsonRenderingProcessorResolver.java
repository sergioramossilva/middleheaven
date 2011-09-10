package org.middleheaven.web.rendering;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.global.text.JsonFormatter;
import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.HttpProcessIOException;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.action.RequestResponseWebContext;


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
			HttpServletResponse response = context.getServletResponse();
			HttpServletRequest request = context.getServletRequest();

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
