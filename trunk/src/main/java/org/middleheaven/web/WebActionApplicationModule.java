package org.middleheaven.web;

import org.middleheaven.application.ApplicationContext;
import org.middleheaven.application.ApplicationID;
import org.middleheaven.web.processing.HttpServerService;
import org.middleheaven.web.processing.UrlMapping;
import org.middleheaven.web.processing.action.ActionBasedProcessor;
import org.middleheaven.web.processing.action.BuildableWebCommandMappingService;
import org.middleheaven.web.processing.action.PresenterCommandMappingBuilder;
import org.middleheaven.web.processing.action.URLMappingBuilder;

/**
 * Represent an action based application running the the web container.
 */
public abstract class WebActionApplicationModule  extends WebApplicationModule{


	private BuildableWebCommandMappingService mappingService  = new BuildableWebCommandMappingService();
	private HttpServerService serverService;
	
	public WebActionApplicationModule(ApplicationID applicationID) {
		super(applicationID);
	}

	protected HttpServerService getHttpServerService(){
		return this.serverService;
	} 
	
	@Override
	protected final void doLoad(ApplicationContext context,HttpServerService serverService) {
		
		this.serverService = serverService;
		ActionBasedProcessor processor = new ActionBasedProcessor(mappingService);
		
		serverService.registerHttpProcessor(this.getApplicationID().toString() + "_processor", processor, UrlMapping.matchAll());
		
		configurate(context);
	}
	
	
	public BuildableWebCommandMappingService getBuildableMappingService() {
		return mappingService  ;
	}

	public void setBuildableMappingService(BuildableWebCommandMappingService mappingService) {
		this.mappingService = mappingService;
	}
	
	/**
	 * utility method
	 * @param presenter
	 * @return
	 */
	public PresenterCommandMappingBuilder map(Class<?> presenter){
		return this.mappingService.map(presenter);
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
	
	
}
