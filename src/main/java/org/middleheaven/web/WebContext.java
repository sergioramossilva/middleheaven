package org.middleheaven.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.middleheaven.io.repository.EmptyFileRepository;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.upload.UploadManagedFileRepository;
import org.middleheaven.util.conversion.TypeConvertions;


public class WebContext extends AbstractContext {

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final Map<String,String> parameters;
	
	private HttpServices service;
	
	@SuppressWarnings("unchecked")
	public WebContext(HttpServices service, HttpServletRequest request, HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
		this.service = service;
		
		if (ServletFileUpload.isMultipartContent(request)){

			parameters = new TreeMap<String,String>();
			UploadManagedFileRepository rep = new UploadManagedFileRepository(request);
	
			this.setAttribute(ContextScope.REQUEST, Context.UPLOADS_FILE_REPOSITORY, rep);
		} else {
			parameters = request.getParameterMap();
			this.setAttribute(ContextScope.REQUEST, Context.UPLOADS_FILE_REPOSITORY, new EmptyFileRepository());
		}
	}
	
	public HttpServices getHttpService(){
		return service;
	}

	public ManagedFileRepository getUploadFileSystem(){
		return this.getAttribute(ContextScope.REQUEST, Context.UPLOADS_FILE_REPOSITORY,ManagedFileRepository.class);
	}
	
	public ManagedFileRepository getApplicationContextFileSystem(){
		return this.getAttribute(ContextScope.REQUEST, Context.APPLICATION_CONTEXT_FILE_REPOSITORY,ManagedFileRepository.class);
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		Object value;
		switch (scope){
		case CONFIGURATION:
			value = request.getSession().getServletContext().getInitParameter(name);
			break;
		case APPLICATION:
			value = request.getSession().getServletContext().getAttribute(name);
			break;
		case SESSION:
			value = request.getSession().getAttribute(name);
			break;
		case REQUEST:
			value = request.getAttribute(name);
			break;
		case PARAMETERS:
			value = parameters.get(name);
			if (value!=null && value.getClass().isArray()){
				value = ((String[])value)[0];
			}
			break;
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}

		if (type==null){
			return (T)value;
		}
		return TypeConvertions.convert(value, type);

	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		
		switch (scope){
		case APPLICATION:
			request.getSession().getServletContext().setAttribute(name,value);
			break;
		case SESSION:
			request.getSession().setAttribute(name,value);
			break;
		case REQUEST:
			request.setAttribute(name,value);
			break;
		case PARAMETERS:
		case CONFIGURATION:
			throw new IllegalArgumentException(scope + " is read-only");
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getAttributeNames(ContextScope scope) {
		switch (scope){
		case CONFIGURATION:
			return request.getSession().getServletContext().getInitParameterNames();
		case APPLICATION:
			return request.getSession().getServletContext().getAttributeNames();
		case SESSION:
			return request.getSession().getAttributeNames();
		case REQUEST:
			return request.getAttributeNames();
		case PARAMETERS:
			return Collections.enumeration(this.parameters.keySet());
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}
	}



	

}
