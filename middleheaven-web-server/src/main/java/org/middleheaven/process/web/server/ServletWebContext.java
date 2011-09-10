package org.middleheaven.process.web.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.EmptyFileRepository;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.io.repository.StreamBasedMediaManagedFileContent;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ScopeAttributeContext;
import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.process.web.server.action.ServletCookieBagTranslator;
import org.middleheaven.process.web.server.global.HttpCultureResolver;
import org.middleheaven.util.coersion.TypeCoercing;


public abstract class ServletWebContext extends WebContext {


	private HttpCultureResolver httpCultureResolver;

	public ServletWebContext(HttpCultureResolver httpCultureResolver) {
		this.httpCultureResolver = httpCultureResolver;
	}
	
	
	protected abstract ServletResponse getServletResponse();
	protected abstract ServletRequest getServletRequest();
	protected abstract HttpSession getSession();
	
	protected abstract ServletContext getServletContext();

	protected abstract void setHeaderAttribute(ContextScope scope, String name, Object value);

	
	private HttpServerRequest request;
	private HttpServerResponse response;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpServerRequest getRequest() {
		if (request == null){
			request = new ServletBasedHttpServerRequest(this.getServletRequest(), this.httpCultureResolver, this.getParameters());
		}
		return request;
	}

	/**
	 * @return
	 */
	protected abstract Map<String, String> getParameters();


	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpServerResponse getResponse() {
		if (response == null){
			response = new ServletBasedHttpServerResponse((HttpServletResponse) this.getServletResponse());
		}
		return response;
	}


	@Override
	public ManagedFileRepository getUploadRepository() {
		return EmptyFileRepository.repository();
	}
	
	@Override
	public final String getContextPath() {
		if (getRequest() instanceof HttpServletRequest){
			return ((HttpServletRequest)getRequest()).getContextPath();
		} else {
			return "";
		}
	}
	

	
	
	
//	@Override
//	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
//		Object value = null;
//		switch (scope){
//		case CONFIGURATION:
//			value = getServletContext().getInitParameter(name);
//			break;
//		case APPLICATION:
//			value = getServletContext().getAttribute(name);
//			break;
//		case SESSION:
//			value = getSession().getAttribute(name);
//			break;
//		case REQUEST:
//			value = getRequest().getAttribute(name);
//			
//			if (value == null){
//				// search special
//				if (WebContext.REQUEST_REMOTE_ADDRESS.equals(name)){
//					value = getRequest().getRemoteAddr();
//				} if (WebContext.REQUEST_REMOTE_HOST.equals(name)){
//					value = getRequest().getRemoteHost();
//				} if (WebContext.REQUEST_SERVER_PORT.equals(name)){
//					value = getRequest().getServerPort();
//				} if (WebContext.REQUEST_SERVER_NAME.equals(name)){
//					value = getRequest().getServerName();
//				} else if (this.getRequest() instanceof HttpServletRequest){
//					// HTTP Only
//					if (WebContext.REQUEST_URL.equals(name)){
//						value = ((HttpServletRequest)this.getRequest()).getRequestURL();
//					} 
//				} 
//			}
//			
//			break;
//		case REQUEST_HEADERS:
//			if (getRequest() instanceof HttpServletRequest){
//				value = ((HttpServletRequest)getRequest()).getHeader(name);
//			} 
//			break;
//		case PARAMETERS:
//			value = getParameters().get(name);
//			
//			if (value!=null){
//				// the result is an array and the expected type is array
//				if (type.isArray() && value.getClass().isArray()){
//					try{
//						return type.cast(value);
//					} catch (ClassCastException e ){
//						throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
//					}
//				} else if (value.getClass().isArray()){
//					value = ((String[])value)[0];
//				}
//			} else if (type.isArray()){ // value is null and is expected array
//				value = new String[0]; // parameters can only be Strings, so an array can only of string
//			}
//			break;
//		case REQUEST_COOKIES:
//			ServletCookieBagTranslator t = new ServletCookieBagTranslator(((HttpServletRequest)getRequest()));
//		
//			if (type.isArray() && type.getComponentType().isAssignableFrom(HttpCookie.class)){
//				return type.cast(t.readAll().toArray());
//			} else if (type.isAssignableFrom(HttpCookie.class)){
//				return type.cast(t.readAll().getCookie(name));
//			} else {
//				throw new IllegalArgumentException("Illegal type for scope " + scope);
//			}
//		default:
//			throw new IllegalArgumentException("Unavailable scope " + scope);
//		}
//
//		if (type==null){
//			// do not convert or cast
//			@SuppressWarnings("unchecked") T t = (T)value;
//			return t;
//		}
//		return TypeCoercing.coerce(value, type);
//
//	}


//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void removeAttribute(ContextScope scope, String name) {
//		switch (scope){
//		case APPLICATION:
//			getServletContext().removeAttribute(name);
//			break;
//		case SESSION:
//			getSession().removeAttribute(name);
//			break;
//		case REQUEST:
//			getServletRequest().removeAttribute(name);
//			break;
//		case REQUEST_COOKIES:
//		case PARAMETERS:
//		case REQUEST_HEADERS:
//		case CONFIGURATION:
//			throw new IllegalArgumentException(scope + " is read-only");
//		default:
//			throw new IllegalArgumentException("Unavailable scope " + scope);
//		}
//	}
//	
//	@Override
//	public void setAttribute(ContextScope scope, String name, Object value) {
//		
//		switch (scope){
//		case APPLICATION:
//			getServletContext().setAttribute(name,value);
//			break;
//		case SESSION:
//			getSession().setAttribute(name,value);
//			break;
//		case REQUEST:
//			getServletRequest().setAttribute(name,value);
//			break;
//		case REQUEST_HEADERS:
//			setHeaderAttribute( scope,  name,  value);
//			break;
//		case REQUEST_COOKIES:
//			if (!(value instanceof HttpCookie)){
//				throw new IllegalArgumentException(HttpCookie.class + " type expected");
//			}
//			
//			ServletCookieBagTranslator t = new ServletCookieBagTranslator(((HttpServletResponse)getResponse()));
//			
//			t.write((HttpCookie)value);
//			break;
//		case PARAMETERS:
//		case CONFIGURATION:
//			throw new IllegalArgumentException(scope + " is read-only");
//		default:
//			throw new IllegalArgumentException("Unavailable scope " + scope);
//		}
//	}

}
