package org.middleheaven.process.web.server.action;

import org.middleheaven.application.ApplicationContext;
import org.middleheaven.application.web.WebModule;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.process.web.UrlMapping;
import org.middleheaven.util.StringUtils;
/**
 * Represent an action based application running the the web container.
 */
public abstract class WebActionModule  extends WebModule{


	private AnnotationDrivenWebCommandMappingService mappingService;
	private WiringService wiringService;

	public WebActionModule() {

	}
	
	private String processorID() {
		return this.getModuleVersion().toString() + "_processor";
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void configurateModule(ApplicationContext context){
	
		WiringService wiringService = context.getServiceContext().getService(WiringService.class);
		
		this.mappingService = new AnnotationDrivenWebCommandMappingService(wiringService);
		
		ActionBasedProcessor processor = new ActionBasedProcessor(mappingService);

		// TODO add module context suffix
		this.getHttpServerService().registerHttpProcessor(processorID(), processor, UrlMapping.matchAll());

	
		configurate(context);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void stop(ApplicationContext context) {

		this.getHttpServerService().unRegisterHttpProcessor(processorID());
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
			String[] parts = StringUtils.split(url, '.');
			String presenter = parts[0]; 
			builder.withNoAction()
			.onSuccess().forwardTo(presenter.concat("/sucess.jsp"))
			.onInvalid().forwardTo(presenter.concat("/invalid.jsp"))
			.onFailure().forwardTo("error.jsp")
			.onError().forwardTo("error.jsp");
		}
	}


	protected abstract void configurate(ApplicationContext context);


}
