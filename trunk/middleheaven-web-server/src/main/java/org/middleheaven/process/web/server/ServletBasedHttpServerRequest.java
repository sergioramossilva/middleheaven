/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.middleheaven.aas.Subject;
import org.middleheaven.global.Culture;
import org.middleheaven.process.AbstractAttributeContext;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ContextScopeStrategy;
import org.middleheaven.process.web.BrowserInfo;
import org.middleheaven.process.web.HttpChannel;
import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.process.web.HttpEntry;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpProcessingUtils;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.HttpUserAgent;
import org.middleheaven.process.web.server.action.ServletCookieBagTranslator;
import org.middleheaven.process.web.server.global.HttpCultureResolver;
import org.middleheaven.util.OperatingSystemInfo;

/**
 * 
 */
class ServletBasedHttpServerRequest implements HttpServerRequest {

	private ServletRequest servletRequest;
	private HttpCultureResolver httpCultureResolver;
	private List<Culture> cultures;
	private AttributeContext attributeContext;
	private Map<String, String > parameters;
	
	/**
	 * Constructor.
	 * @param servletRequest
	 */
	public ServletBasedHttpServerRequest(ServletRequest servletRequest, HttpCultureResolver httpCultureResolver, Map<String, String > parameters) {
		this.servletRequest = servletRequest;
		this.httpCultureResolver = httpCultureResolver;
		this.parameters = parameters;
		
	}
	
	/**
	 * @param scope
	 * @return
	 */
	public ContextScopeStrategy resolveScopeAttributeContext(ContextScope scope) {
		if (servletRequest instanceof HttpServletRequest){
			HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
			
			switch (scope){
			case CONFIGURATION:
				return new InitConfigurationContextScopeStrategy(httpRequest.getSession().getServletContext());
			case APPLICATION:
				return new ApplicationContextScopeStrategy(httpRequest.getSession().getServletContext());
			case SESSION:
				return new SessionContextScopeStrategy(httpRequest.getSession());
			case REQUEST:
				return new RequestContextScopeStrategy(httpRequest);
//				if (value == null){
//					// search special
//					if (WebContext.REQUEST_REMOTE_ADDRESS.equals(name)){
//						value = getRequest().getRemoteAddr();
//					} if (WebContext.REQUEST_REMOTE_HOST.equals(name)){
//						value = getRequest().getRemoteHost();
//					} if (WebContext.REQUEST_SERVER_PORT.equals(name)){
//						value = getRequest().getServerPort();
//					} if (WebContext.REQUEST_SERVER_NAME.equals(name)){
//						value = getRequest().getServerName();
//					} else if (this.getRequest() instanceof HttpServletRequest){
//						// HTTP Only
//						if (WebContext.REQUEST_URL.equals(name)){
//							value = ((HttpServletRequest)this.getRequest()).getRequestURL();
//						} 
//					} 
//				}
			case REQUEST_HEADERS:
				return new RequestHeadersContextScopeStrategy(httpRequest);
			case PARAMETERS:
				return new ParametersContextScopeStrategy(parameters);
			case REQUEST_COOKIES:
				return new RequestCookiesContextScopeStrategy(httpRequest);
				
			default:
				throw new IllegalArgumentException("Unavailable scope " + scope);
			}
		} else {
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}
		
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeContext getAttributes() {
		return attributeContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpMethod getMethod() {
		if (servletRequest instanceof HttpServletRequest){
			return HttpMethod.valueOf(( (HttpServletRequest) servletRequest ).getMethod().toUpperCase());
		} else {
			return HttpMethod.UNKOWN;
		}
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public HttpUserAgent getAgent(){
		if (servletRequest instanceof HttpServletRequest){
			return HttpProcessingUtils.parse( (HttpServletRequest) servletRequest);
		} else {
			// it is not a HttpRequest, create unkown info
			return new HttpUserAgent(BrowserInfo.unkownBrowser(), OperatingSystemInfo.unkown()
			);
		}
		
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public HttpUrl getRequestUrl() {
		if (servletRequest instanceof HttpServletRequest){
			HttpServletRequest hrequest = (HttpServletRequest) servletRequest;
			return new HttpUrl(hrequest.getRequestURL(), hrequest.getContextPath());
		} else {
			return new HttpUrl(null, "/");
		}
		
	}
	
	@Override
	public HttpUrl getRefererUrl() {
		if (servletRequest instanceof HttpServletRequest){
			HttpServletRequest hrequest = (HttpServletRequest) servletRequest;
			String referer = ((HttpServletRequest) hrequest).getHeader("Referer");
			return new HttpUrl( referer,  hrequest.getContextPath());
		} else {
			return null;
		}
	
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpEntry getEntry() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Culture getCulture() {
		return this.httpCultureResolver.resolveFrom(this);
	}

	@SuppressWarnings("unchecked")
		/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Culture> getCultures() {
		
		if (cultures == null) {
			if (this.servletRequest instanceof HttpServletRequest){
				// read the Accept-Language header manually because request.getLocale()
				// defaults to Locale.getDefaultLocale() when the header is not present
				// this will deceive the globalization api
				
				this.cultures = HttpProcessingUtils.acceptableLanguages( ((HttpServletRequest) servletRequest ).getHeader("Accept-Language"));
			} else {
				
			    this.cultures = new ArrayList<Culture>();
				Enumeration<Locale> enumeration = servletRequest.getLocales();
				
				while (enumeration.hasMoreElements()){
					cultures.add(Culture.valueOf(enumeration.nextElement()));
				}

			}
		}
		return cultures;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpChannel getHttpChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Subject getAuthenticatedSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	
	private class ServletRequestAttributeContext extends AbstractAttributeContext {
		
		private final Map<ContextScope , ContextScopeStrategy> contexts = new EnumMap<ContextScope , ContextScopeStrategy>(ContextScope.class);
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
			return getScopeAttributeContext(scope).getAttribute(name, type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setAttribute(ContextScope scope, String name, Object value) {
			 getScopeAttributeContext(scope).setAttribute(name, value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeAttribute(ContextScope scope, String name) {
			 getScopeAttributeContext(scope).removeAttribute(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ContextScopeStrategy getScopeAttributeContext(ContextScope scope) {
			
			ContextScopeStrategy context = (ContextScopeStrategy) contexts.get(scope);
			if (context == null) {
				context = ServletBasedHttpServerRequest.this.resolveScopeAttributeContext(scope);
				contexts.put(scope,context);
			}
			return context;
			
			
		}
		
	}






}
