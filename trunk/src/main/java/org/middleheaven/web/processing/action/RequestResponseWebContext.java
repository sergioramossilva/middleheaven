package org.middleheaven.web.processing.action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.middleheaven.io.repository.EmptyFileRepository;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.upload.UploadManagedFileRepository;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.HttpProcessingUtils;
import org.middleheaven.web.processing.HttpUrl;
import org.middleheaven.web.processing.HttpUserAgent;

public class RequestResponseWebContext extends ServletWebContext {

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final Map<String,String> parameters;
	private ManagedFileRepository uploadRepository;

	@SuppressWarnings("unchecked")
	public RequestResponseWebContext(HttpServletRequest request,HttpServletResponse response) {
		this.request = request;
		this.response = response;
		
		if (ServletFileUpload.isMultipartContent(request)){

			parameters = new TreeMap<String,String>();
			uploadRepository = UploadManagedFileRepository.repositoryOf(request,parameters);

		} else {
			parameters = request.getParameterMap();
			uploadRepository = EmptyFileRepository.repository();
		}
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
