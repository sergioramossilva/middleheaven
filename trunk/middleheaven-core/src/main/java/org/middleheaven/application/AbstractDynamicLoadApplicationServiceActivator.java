package org.middleheaven.application;

import java.util.Collection;
import java.util.Map;

import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.bootstrap.FileContextService;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.logging.LogServiceDelegatorLogger;
import org.middleheaven.logging.Logger;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.Version;

/**
 * Provides a support implementation for {@link ApplicationService} enabling loading application 
 * modules present at the application configuration path.
 * 
 * 
 */
public abstract class AbstractDynamicLoadApplicationServiceActivator extends ServiceActivator implements BootstapListener  {


	private ModularApplicationService applicationService;

	ApplicationVersion application;
	private FileContextService fileContextService;
	ServiceContext serviceContext;
	
	Logger log;
	
	public AbstractDynamicLoadApplicationServiceActivator(){}

	protected FileContextService getFileContextService(){
		return this.fileContextService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(BootstrapService.class));
		specs.add(new ServiceSpecification(LoggingService.class));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(ApplicationService.class));
	}
	
	
	@Override
	public void activate(ServiceContext serviceContext) {
		
		this.serviceContext = serviceContext;
		
		fileContextService = serviceContext.getService(FileContextService.class);
		
		log = new LogServiceDelegatorLogger(this.getClass().getName(), serviceContext.getService(LoggingService.class));
		
		BootstrapService bootstrapService = serviceContext.getService(BootstrapService.class);

		bootstrapService.addListener(this);
		applicationService =  new ModularApplicationService(this);
		
		serviceContext.register(ApplicationService.class, applicationService);
	}

	@Override
	public void inactivate(ServiceContext serviceContext) {
		applicationService.stop();
	}

	@Override
	public void onBoostapEvent(BootstrapEvent event) {
		if(event.isBootup() && event.isEnd()){
			// start cycle
			applicationService.start();
		} else if (event.isBootdown() && event.isStart()){
			// end cycle
			applicationService.stop();
		}
	};

	protected void addModule(ModuleVersion module){
		this.applicationService.addModule(module);
	}

	/**
	 * @param app
	 */
	protected final void setApplication(ApplicationVersion app) {
		this.application = app;
	}
	
	protected final boolean setState(ApplicationCycleState phase){
		return this.applicationService.setState(phase);
	}
	
	protected Logger getLog() {
		return log;
	}


	protected final void parseAttributes(Map<String, String> attributes , ClassLoader cloader){

		String applicationSignature = attributes.get("Application");

		if (applicationSignature == null){
			throw new IllegalStateException("No application definition found");
		}

		String applicationListenersSignature = attributes.get("Application-Listeners");

		String applicationModulesSignature = attributes.get("Application-Modules");

		String applicationModuleListenersSignature = attributes.get("Application-Module-Listeners");


		ApplicationVersion app = parseApplicationSignature(applicationSignature);
		
		setApplication(app);
		
		parseAndAddModules(app, applicationModulesSignature , cloader);


		ClassSet appListeners = parseApplicationListeners(applicationListenersSignature, cloader);

		ClassSet moduleListeners = parseModulesListeners(applicationModuleListenersSignature, cloader);

		// TODO add classe sets
	
	}



	/**
	 * @param app
	 * @param applicationModulesSignature
	 * @param cloader 
	 */
	private void parseAndAddModules(ApplicationVersion app, String applicationModulesSignature, ClassLoader cloader) {

		String[] types = StringUtils.split(applicationModulesSignature.trim(), ',');

		ClassSet set = new ClassSet();

		if (applicationModulesSignature.trim().length() == 0){
			throw new IllegalStateException("No modules found");
		}
		
		WiringService wiringService = serviceContext.getService(WiringService.class);
		
		for (String type : types){
			ModuleActivator m =  Introspector.of(ModuleActivator.class).load(type.trim(),cloader).newInstance();
			
			wiringService.wireMembers(m);
	
			m.start(applicationService.appContext);
		}

		//this.wiringService.addItemBundle(new ClassSetWiringBundle(set));
		
	}

	/**
	 * Parse name=0.0.1
	 * @param applicationSignature
	 * @return
	 */
	private ApplicationVersion parseApplicationSignature(String applicationSignature) {
		String[] split = StringUtils.split(applicationSignature.trim(), '=');

		return new ApplicationVersion(split[0], Version.valueOf(split[1]));
	}

	private ClassSet  parseApplicationListeners (String applicationListenersSignature , ClassLoader cloader){
		ClassSet listeners = new ClassSet();

		if (applicationListenersSignature == null || applicationListenersSignature.trim().length() == 0){
			return listeners;
		}
		
		String[] types = StringUtils.split(applicationListenersSignature.trim(),',');

		for (String type : types){
			listeners.add((Class<? extends ApplicationListener>) Introspector.of(ApplicationListener.class).load(type.trim(),cloader).getIntrospected());
		}

		return listeners;
	}

	private ClassSet  parseModulesListeners (String applicationModuleListenersSignature , ClassLoader cloader){
		ClassSet listeners = new ClassSet();

		if (applicationModuleListenersSignature == null || applicationModuleListenersSignature.trim().length() == 0){
			return listeners;
		}
		
		
		String[] types = StringUtils.split(applicationModuleListenersSignature,',');

		for (String type : types){
			listeners.add(Introspector.of(ModuleListener.class).load(type.trim(),cloader).getIntrospected());
		}

		return listeners;
	}

	protected abstract void loadPresentModules();



}
