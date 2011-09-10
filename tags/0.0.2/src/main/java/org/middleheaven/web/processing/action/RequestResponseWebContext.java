package org.middleheaven.web.processing.action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.upload.UploadFilesContext;
import org.middleheaven.io.repository.upload.UploadFilesRequestAnalyzer;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.HttpProcessingUtils;
import org.middleheaven.web.processing.HttpUrl;
import org.middleheaven.web.processing.HttpUserAgent;
import org.middleheaven.web.processing.global.HttpCultureResolver;

public class RequestResponseWebContext extends ServletWebContext {

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final Map<String,String> parameters;
	private ManagedFileRepository uploadRepository;

	public RequestResponseWebContext(HttpServletRequest request,HttpServletResponse response, HttpCultureResolver httpCultureResolveService) {
		super(httpCultureResolveService);
		
		this.request = request;
		this.response = response;
		
		this.request.setAttribute("__" + HttpCultureResolver.class.getName(), httpCultureResolveService);
		
		UploadFilesContext context = UploadFilesRequestAnalyzer.getContext(request);
		
		this.parameters = context.getParametersMap();
		this.uploadRepository = context.getManagedFileRepository();
		
	}

	@Override
	public ManagedFileRepository getUploadRepository() {
		return uploadRepository;
	}

	public HttpUserAgent getAgent(){
		return HttpProcessingUtils.parse(request);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public Map<String, String> getParameters() {
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
	public HttpMethod getHttpService() {
		return HttpMethod.valueOf(request.getMethod().toUpperCase());
	}

	@Override
	public HttpUrl getRequestUrl() {
		return new HttpUrl(request.getRequestURL(), this.getContextPath());
	}

	@Override
	public String getContextPath() {
		return request.getContextPath();
	}

	@Override
	public InetAddress getRemoteAddress(){
		try{
			return InetAddress.getByName(request.getRemoteAddr());
		}catch (UnknownHostException e){
			return null;
		}
	}



}
