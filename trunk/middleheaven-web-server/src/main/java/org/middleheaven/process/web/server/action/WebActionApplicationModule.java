package org.middleheaven.process.web.server.action;

import org.middleheaven.application.ApplicationContext;
import org.middleheaven.application.ApplicationID;
import org.middleheaven.application.web.WebMainApplicationModule;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.process.web.UrlMapping;
import org.middleheaven.process.web.server.action.ActionBasedProcessor;
/**
 * Represent an action based application running the the web container.
 */
public abstract class WebActionApplicationModule  extends WebMainApplicationModule{


	private AnnotationDrivenWebCommandMappingService mappingService;
	public WebActionApplicationModule(ApplicationID applicationID) {
		super(applicationID);
	}
	
	@Wire
	public void setBuildableMappingService(AnnotationDrivenWebCommandMappingService mappingService){
		this.mappingService = mappingService;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final void load(ApplicationContext context) {

		ActionBasedProcessor processor = new ActionBasedProcessor(mappingService);

		this.getHttpServerService().registerHttpProcessor(this.getApplicationID().toString() + "_processor", processor, UrlMapping.matchAll());

		configurate(context);
	}

	public BuildableWebCommandMappingService getBuildableMappingService() {
		return mappingService  ;
	}


	/**
	 * utility method
	 * @param presenter
	 * @return
	 */
	public PresenterCommandMappingBuilder map(Class<?> presenter){
		return this.mappingService.map(presenter);
	}

	public void scan(ClassSet classSet){
		this.mappingService.scan(classSet);
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

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void unload(ApplicationContext context) {

		this.getHttpServerService().unRegisterHttpProcessor("id");
	}

	protected abstract void configurate(ApplicationContext context);


}
