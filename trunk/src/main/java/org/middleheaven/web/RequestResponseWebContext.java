package org.middleheaven.web;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.middleheaven.global.Culture;
import org.middleheaven.io.repository.EmptyFileRepository;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.upload.UploadManagedFileRepository;
import org.middleheaven.ui.ContextScope;

public class RequestResponseWebContext extends ServletWebContext{

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final Map<String,String> parameters;

	@SuppressWarnings("unchecked")
	public RequestResponseWebContext(HttpServletRequest request,HttpServletResponse response) {
		this.request = request;
		this.response = response;

		
		if (ServletFileUpload.isMultipartContent(request)){

			parameters = new TreeMap<String,String>();
			ManagedFileRepository vfs = UploadManagedFileRepository.repositoryOf(request,parameters);
			this.setAttribute(ContextScope.REQUEST, WebContext.UPLOADS_FILESYSTEM, vfs);
		} else {
			parameters = request.getParameterMap();
			this.setAttribute(ContextScope.REQUEST, WebContext.UPLOADS_FILESYSTEM, EmptyFileRepository.getRepository());
		}
	}

	public Agent getAgent(){
		return Agent.parse(request);
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	
	
	@Override
	protected Map<String, String> getParameters() {
		return this.parameters;
	}

	protected javax.servlet.ServletContext getServletContext() {
		return request.getSession().getServletContext();
	}

	@Override
	protected HttpSession getSession() {
		return request.getSession();
	}

	@Override
	protected void setHeaderAttribute(ContextScope scope, String name,Object value) {
		 if (!( value instanceof String)){
			 throw new ClassCastException(value + " cannot be used as a header value");
		 }
		 
		 response.setHeader(name, value.toString());
	}

	@Override
	public HttpServices getHttpService() {
		return HttpServices.valueOf(request.getMethod().toUpperCase());
	}

	@Override
	public Culture getCulture() {
		return Culture.valueOf(request.getLocale());
	}
}
