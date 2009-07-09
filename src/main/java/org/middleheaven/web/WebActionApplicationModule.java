package org.middleheaven.web;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.application.ApplicationContext;
import org.middleheaven.application.ApplicationID;
import org.middleheaven.web.processing.HttpServerService;
import org.middleheaven.web.processing.UrlMapping;
import org.middleheaven.web.processing.action.ActionBasedProcessor;
import org.middleheaven.web.processing.action.Interceptor;
import org.middleheaven.web.processing.action.PresenterCommandMappingBuilder;
import org.middleheaven.web.processing.action.URLMappingBuilder;
import org.middleheaven.web.processing.action.WebCommandMapping;
import org.middleheaven.web.processing.action.WebCommandMappingService;

/**
 * Represent an action based application running the the web container.
 */
public abstract class WebActionApplicationModule  extends WebApplicationModule{

	private final List<WebCommandMapping> mappings = new CopyOnWriteArrayList<WebCommandMapping>();

	public WebActionApplicationModule(ApplicationID applicationID) {
		super(applicationID);
	}


	@Override
	protected final void doLoad(ApplicationContext context,HttpServerService serverService) {
		
		ActionBasedProcessor processor = new ActionBasedProcessor(mappingService);
		
		serverService.registerHttpProcessor(this.getApplicationID().toString() + "_processor", processor, new UrlMapping(){

			@Override
			public boolean match(String url) {
				return true;
			}
			
		});
		
		configurate(context);
	}
	
	
	public WebCommandMappingService getMappingService() {
		return mappingService;
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
		map(Index.class).to("").withNoAction().onSuccess().redirectTo(targetUrl);
	}
	
	/**
	 * Maps all requests directed to the specified internal URL to be handle by the specified presenter class
	 * @param url
	 * @param presenter
	 * @return
	 */
	public <P> PresenterCommandMappingBuilder map(Class<P> presenter){
		
		PresenterCommandMappingBuilder builder = PresenterCommandMappingBuilder.map(presenter);
		this.mappings.add(builder.getMapping());
		return builder;
	}
	
	protected void setDefaults(URLMappingBuilder builder ,String url ){
		// set defaults
		if (url.indexOf(".")>=0){
			String[] parts = url.substring(1).split("\\.");
			String presenter = parts[0]; 
			builder.withNoAction()
			.onSuccess().forwardTo(presenter.concat("/sucess.jsp"))
			.onInvalid().forwardTo(presenter.concat("/invalid.jsp"))
			.onFailure().forwardTo("error.jsp")
			.onError().forwardTo("error.jsp");
		}
	}
	
	@Override
	protected void doUnload(ApplicationContext context,
			HttpServerService serverService) {

		serverService.unRegisterHttpProcessor("id");
	}
	
	protected abstract void configurate(ApplicationContext context);
	
	
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
	
}
