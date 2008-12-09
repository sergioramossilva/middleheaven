package org.middleheaven.web;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.ui.Context;

/**
 * Represent the application running the the web container.
 * This class must be extended by each application and the qualified name registred in the web.xml file
 * in the <code>web-application-class</code> parameter. Example:
 * 	<context-param>
 * 		<param-name>web-application-class</param-name>
 * 		<param-value>my.application.package.MyApplication</param-value>
	</context-param>
 */
public abstract class WebApplication {

	private final List<WebCommandMapping> mappings = new CopyOnWriteArrayList<WebCommandMapping>();

	private WebCommandMappingService mappingService = new WebCommandMappingService(){

		@Override
		public void addInterceptor(Interceptor interceptor, CharSequence url) {
			// TODO implement .addInterceptor
			
		}

		@Override
		public WebCommandMapping resolve(CharSequence url) {
			for (WebCommandMapping m : mappings){
				if (m.matches(url)){
					return m;
				}
			}
			return null;
		}
		
	};
	

	public WebCommandMappingService getMappingService() {
		return mappingService;
	}

	/**
	 * Changes the the view base. The view base is an internal URL (relative to the web context) 
	 * where the JSP (or other interface files) are located 
	 * @param viewBase the relative path to the view folder
	 */
	public void setViewBase(String viewBase) {
		GlobalMappings.setViewBase(viewBase);
	}
	
	/**
	 * 
	 * @return the view base
	 * @see WebApplication#setViewBase(String)
	 */
	public String getViewBase() {
		return GlobalMappings.getViewBase();
	}

	public void setMappingService(WebCommandMappingService mappingService) {
		this.mappingService = mappingService;
	}
	
	public static class Index{}

	/**
	 * Maps the specified internal URL to all requests coming from the context root.   
	 * @param targetUrl
	 */
	protected void mapIndex(String targetUrl) {
		 map("", Index.class).onSuccess().forwardTo( targetUrl);
	}
	
	/**
	 * Maps all requests directed to the specified internal URL to be handle by the specified presenter class
	 * @param url
	 * @param presenter
	 * @return
	 */
	protected PresenterCommandMappingBuilder map(String url, Class<?> presenter) {

		PresenterCommandMappingBuilder builder = PresenterCommandMappingBuilder.map(presenter).to("/" + url);
		this.mappings.add(builder.getMapping());
		
		setDefaults(builder, url);
		
	
		return builder;
	}
	
	protected void setDefaults(PresenterCommandMappingBuilder builder ,String url ){
		// set defaults
		String[] parts = url.substring(1).split("\\.");
		String presenter = parts[0]; 
		builder.onSuccess().forwardTo(presenter.concat("/sucess.jsp"))
		.onInvalid().forwardTo(presenter.concat("/invalid.jsp"))
		.onFailure().forwardTo("error.jsp")
		.onError().forwardTo("error.jsp");
	}
	
	
	
	public void start(Context context){
		this.configurate(context);
	}
	
	protected abstract void configurate(Context context);
}
