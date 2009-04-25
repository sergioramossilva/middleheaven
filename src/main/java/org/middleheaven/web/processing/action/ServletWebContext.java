package org.middleheaven.web.processing.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.middleheaven.global.Culture;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.EmptyFileRepository;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.io.repository.StreamBasedManagedFile;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.CulturalAttributeContext;
import org.middleheaven.util.OperatingSystemInfo;
import org.middleheaven.util.conversion.TypeConvertions;
import org.middleheaven.web.processing.BrowserInfo;
import org.middleheaven.web.processing.HttpProcessingUtils;
import org.middleheaven.web.processing.HttpUserAgent;


public abstract class ServletWebContext extends WebContext implements CulturalAttributeContext{

	protected abstract ServletResponse getResponse();
	protected abstract ServletRequest getRequest();
	protected abstract HttpSession getSession();
	
	protected abstract ServletContext getServletContext();

	protected abstract void setHeaderAttribute(ContextScope scope, String name, Object value);

	@Override
	public HttpMethod getHttpService() {
		if (getRequest() instanceof HttpServletRequest){
			return HttpMethod.valueOf(((HttpServletRequest)getRequest()).getMethod().toUpperCase());
		} else {
			return HttpMethod.UNKOWN;
		}
	}
	
	public HttpUserAgent getAgent(){
		if (getRequest() instanceof HttpServletRequest){
			return HttpProcessingUtils.parse((HttpServletRequest)getRequest());
		} else {
			return new HttpUserAgent(BrowserInfo.unkownBrowser(),OperatingSystemInfo.unkown());
		}
		
	}
	
	@Override
	public Culture getCulture() {
		return Culture.valueOf(getRequest().getLocale());
	}

	@Override
	public ManagedFileRepository getUploadRepository() {
		return EmptyFileRepository.repository();
	}
	
	@Override
	public String getContextPath() {
		if (getRequest() instanceof HttpServletRequest){
			return ((HttpServletRequest)getRequest()).getContextPath();
		} else {
			return "";
		}
	}

	@Override
	public StringBuilder getRequestUrl() {
		if (getRequest() instanceof HttpServletRequest){
			return new StringBuilder(((HttpServletRequest)getRequest()).getRequestURL());
		} else {
			return new StringBuilder();
		}
		
	}

	public MediaManagedFile responseMediaFile(){
		
		return new StreamBasedManagedFile("",null){

			boolean streamIsOpen = false;
			String name = "";
			
			public String getName(){
				return name;
			}
			
			@Override
			public void setName(String newName) {
				this.name = newName;
				if (getResponse() instanceof HttpServletResponse){
					((HttpServletResponse)getResponse()).setHeader("Content-disposition", "attachment; filename=".concat(newName));
				}
			}
			
			@Override
			protected InputStream getInputStream() {
				throw new UnsupportedOperationException("Response Virtual File as no input stream associated");
			}

			@Override
			protected OutputStream getOutputStream()  {
				try {
					 OutputStream out = getResponse().getOutputStream();
					 streamIsOpen = true;
					 return out;
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				} 
			}



			@Override
			public boolean setSize(long expectedSize)  {
				if (streamIsOpen){
					throw new ManagedIOException("Configuration must be made before opening streams");
				}
				getResponse().setContentLength((int)expectedSize);
				return true;
			}

			@Override
			public void setContentType(String mimeContentType){
				if (streamIsOpen){
					throw new ManagedIOException("Configuration must be made before opening streams");
				}
				getResponse().setContentType(mimeContentType);
			}

			@Override
			public String getContentType()  {
				throw new UnsupportedOperationException("Content type cannot be read for this file type");
			}

			@Override
			public long getSize() {
				return getResponse().getBufferSize();
			}
			
		};
	}
	
	
	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		Object value = null;
		switch (scope){
		case CONFIGURATION:
			value = getServletContext().getInitParameter(name);
			break;
		case APPLICATION:
			value = getServletContext().getAttribute(name);
			break;
		case SESSION:
			value = getSession().getAttribute(name);
			break;
		case REQUEST:
			value = getRequest().getAttribute(name);
			
			if (value == null){
				// search special
				if (WebContext.REQUEST_REMOTE_ADDRESS.equals(name)){
					value = getRequest().getRemoteAddr();
				} if (WebContext.REQUEST_REMOTE_HOST.equals(name)){
					value = getRequest().getRemoteHost();
				} if (WebContext.REQUEST_SERVER_PORT.equals(name)){
					value = getRequest().getServerPort();
				} if (WebContext.REQUEST_SERVER_NAME.equals(name)){
					value = getRequest().getServerName();
				} else if (this.getRequest() instanceof HttpServletRequest){
					// HTTP Only
					if (WebContext.REQUEST_URL.equals(name)){
						value = ((HttpServletRequest)this.getRequest()).getRequestURL();
					} 
				} 
			}
			
			break;
		case HEADER:
			if (getRequest() instanceof HttpServletRequest){
				value = ((HttpServletRequest)getRequest()).getHeader(name);
			} 
			break;
		case PARAMETERS:
			value = getParameters().get(name);
			
			if (value!=null){
				// the result is an array and the expected type is array
				if (type.isArray() && value.getClass().isArray()){
					try{
						return type.cast(value);
					} catch (ClassCastException e ){
						throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
					}
				} else if (value.getClass().isArray()){
					value = ((String[])value)[0];
				}
			} else if (type.isArray()){ // value is null and is expected array
				value = new String[0]; // parameters can only be Strings, so an array can only of string
			}
			break;
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}

		if (type==null){
			// do not convert or cast
			@SuppressWarnings("unchecked") T t = (T)value;
			return t;
		}
		return TypeConvertions.convert(value, type);

	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		
		switch (scope){
		case APPLICATION:
			getServletContext().setAttribute(name,value);
			break;
		case SESSION:
			getSession().setAttribute(name,value);
			break;
		case REQUEST:
			getRequest().setAttribute(name,value);
			break;
		case HEADER:
			setHeaderAttribute( scope,  name,  value);
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
			return getServletContext().getInitParameterNames();
		case APPLICATION:
			return getServletContext().getAttributeNames();
		case SESSION:
			return getSession().getAttributeNames();
		case REQUEST:
			return getRequest().getAttributeNames();
		case PARAMETERS:
			return Collections.enumeration(this.getParameters().keySet());
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}
	}

	
}
