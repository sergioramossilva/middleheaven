package org.middleheaven.application;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.bootstrap.FileContextService;
import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.logging.LogServiceDelegatorLogger;
import org.middleheaven.logging.Logger;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.Version;

/**
 * Provides a support implementation for {@link ApplicationService} enabling loading application 
 * modules present at the application configuration path
 * 
 * 
 */
public abstract class AbstractDynamicLoadApplicationServiceActivator extends ServiceActivator implements BootstapListener  {


	private DynamicLoadApplicationService applicationService;

	private ApplicationVersion application;
	private FileContextService fileContextService;
	private ServiceContext serviceContext;
	
	private Logger log;
	
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
		applicationService =  new DynamicLoadApplicationService();
		
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
	
	
	
	private class DynamicLoadApplicationService implements ApplicationService {


		private ApplicationContext appContext = new ApplicationContext(){

			@Override
			public ApplicationVersion getApplication() {
				return application;
			}

			@Override
			public Collection<Module> getModules() {
				return Collections.unmodifiableCollection(modules);
			}

			@Override
			public boolean isModulePresent(String name) {
				return modulesNamesMapping.containsKey(name);
			}

			@Override
			public boolean isCompatibleModulePresent(ModuleVersion version) {

				ModuleVersion currentModule = modulesNamesMapping.get(version.getName());

				return currentModule!=null && currentModule.getVersion().compareTo(version.getVersion())>=0;

			}

			@Override
			public ServiceContext getServiceContext() {
				return serviceContext;
			}

		};

		private ApplicationCycleState state = ApplicationCycleState.STOPED;

		private Deque<ModuleVersion> modulesVersions = new LinkedList<ModuleVersion>();
		private Deque<Module> modules = new LinkedList<Module>();
		private Map<String, ModuleVersion> modulesNamesMapping = new HashMap<String, ModuleVersion>();

		private Set<ModuleListener> moduleListeners =  new CopyOnWriteArraySet<ModuleListener>();
		private Set<ApplicationListener> listeners = new CopyOnWriteArraySet<ApplicationListener>();

	

		public DynamicLoadApplicationService() {
//			wiringService.addObjectCycleListener(new ObjectPoolListener(){
//
//				@Override
//				public void onObjectAdded(ObjectEvent event) {
//					if (event.getObject() instanceof Module){
//						modules.add((Module) event.getObject());
//					}
//				}
//
//				@Override
//				public void onObjectRemoved(ObjectEvent event) {
//					if (event.getObject() instanceof Module){
//						modules.remove((Module) event.getObject());
//					}
//				}
//				
//			});
		}


		public void addModule(ModuleVersion module) {
			
			ModuleVersion currentModule = modulesNamesMapping.get(module.getName());
			
			if (currentModule==null){
				// new module
				modulesVersions.add(module);
				modulesNamesMapping.put(module.getName(), module);
				
			} else {
				
				Version candidate = module.getVersion();
				Version current = currentModule.getVersion();
				
				// if it is a newer version
				if (candidate.compareTo(current)>0){
					// unload older
					// TODO currentModule.unload(this);
					
					// install newer
					modulesNamesMapping.put(module.getName(),module);
					modulesVersions.add(module);
				}
			}
		}
		
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addApplicationListener(ApplicationListener listener) {
			this.listeners.add(listener);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeApplicationListener(ApplicationListener listener) {
			this.listeners.remove(listener);
		}

		protected boolean setState(ApplicationCycleState phase){
			if ( state.canChangeTo(phase)){
				state = phase;
				fireEvent(new ApplicationEvent(phase));
				return true;
			}
			return false;
		}

		private void fireEvent (ApplicationEvent event){
			for (ApplicationListener l: listeners){
				l.onCycleStateChanged(event);
			}
		}

		protected void start() {

			if (this.setState(ApplicationCycleState.STOPED)){
				log.info("Scanning for applications");
				loadPresentModules();
				log.info("Activating applications");

				for (Iterator<ModuleVersion> it = this.getModules().descendingIterator(); it.hasNext(); ){
					ModuleVersion module = it.next();

					for (ModuleListener moduleListener :  moduleListeners){

						try {
							if (moduleListener.isListenerOf(module)){
								moduleListener.onStart(module, appContext);
							}

						} catch (RuntimeException e){
							log.error(e  , "Impossible to activate listener {0} for module {1}" , moduleListener.getClass(), module);
							throw e;
						}
					}

				}



				this.setState(ApplicationCycleState.LOADED);
				this.setState(ApplicationCycleState.READY);

				log.info("Applications ready");
			}

		}


		/**
		 * @return
		 */
		private Deque<ModuleVersion> getModules() {
			return modulesVersions;
		}

		protected void stop() {
			this.setState(ApplicationCycleState.PAUSED);
			log.info("Deactivating applications");


			for (Iterator<ModuleVersion> it = this.getModules().iterator(); it.hasNext(); ){
				ModuleVersion module = it.next();

				for (ModuleListener moduleListener : moduleListeners){
					try {
						moduleListener.onStop(module, appContext);
					} catch (Exception e){
						log.warn(e,"Impossible to deactivate listener {0} for module {1}", moduleListener.getClass(),  module);
					}
				}
			}

			this.setState(ApplicationCycleState.STOPED);
			log.info("Applications deactivated");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addModuleListener(ModuleListener listener) {
			this.moduleListeners.add(listener);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeModuleListener(ModuleListener listener) {
			throw new UnsupportedOperationException("Not implememented yet");
		}




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
			Module m =  Introspector.of(Module.class).load(type.trim(),cloader).newInstance();
			
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
