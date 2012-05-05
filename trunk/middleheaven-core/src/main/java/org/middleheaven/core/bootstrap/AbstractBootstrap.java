/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.bootstrap.activation.ActivatorDependencyResolver;
import org.middleheaven.core.bootstrap.activation.ActivatorScanner;
import org.middleheaven.core.bootstrap.activation.ActivatorScannerEvent;
import org.middleheaven.core.bootstrap.activation.ActivatorScannerListener;
import org.middleheaven.core.bootstrap.activation.AnnotationBasedDependencyResolver;
import org.middleheaven.core.dependency.DependencyResolver;
import org.middleheaven.core.dependency.InicializationNotPossibleException;
import org.middleheaven.core.dependency.InicializationNotResolvedException;
import org.middleheaven.core.dependency.Starter;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.RegistryServiceContext;
import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.SetActivatorScanner;
import org.middleheaven.core.wiring.ProfilesBag;
import org.middleheaven.core.wiring.PropertyManagers;
import org.middleheaven.core.wiring.StandardWiringService;
import org.middleheaven.core.wiring.SytemEnvPropertyManager;
import org.middleheaven.core.wiring.SytemPropertyManager;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.global.CultureModelLocalizationService;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.PropertiesFile;
import org.middleheaven.logging.BroadcastLoggingService;
import org.middleheaven.logging.LogServiceDelegatorLogger;
import org.middleheaven.logging.Logger;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.util.StopWatch;
import org.middleheaven.util.StringUtils;

/**
 * Implements the generic inicilaization engine for the MiddleHeaven platform.
 * 
 * Subclasses of {@link AbstractBootstrap} implement
 * bootstrap in different execution environments and enable
 * for the application's execution to be environment independent.
 * 
 * Every bootstrap defines implementations for a set of pre-defined services.
 * The provided services are:
 * 
 *  <ul>
 *  	<li>{@link LoggingService} - Provides the generic mecanics to deliver logging messages</li>
 *  <ul>
 *
 */
public abstract class AbstractBootstrap  {

	private final RegistryServiceContext serviceRegistryContext = new RegistryServiceContext();
	private final PropertyManagers propertyManagers = new PropertyManagers();

	private final List<ServiceActivatorModel> activators = new ArrayList<ServiceActivatorModel>();
	private final Set<ActivatorScanner> scanners = new HashSet<ActivatorScanner>();
	private final Set<ActivatorDependencyResolver> resolvers = new CopyOnWriteArraySet<ActivatorDependencyResolver>();

	
	private BootstrapEnvironmentResolver bootstrapEnvironmentResolver;
	
	private SimpleBootstrapService bootstrapService;
	
	private LoggingService loggingService;
	private WiringService wiringService;
	private Logger logger;
	private BootstrapEnvironment environment;
	private BootstrapContext context;
	
	public AbstractBootstrap (){
		
		// prepare properties.
		this.propertyManagers.addFirst(new SytemEnvPropertyManager());
		this.propertyManagers.addFirst(new SytemPropertyManager());

		this.resolvers.add(new AnnotationBasedDependencyResolver());
		
		this.wiringService = new StandardWiringService(this.propertyManagers);
		
	}

	protected WiringService getWiringService(){
		return this.wiringService;
	}

	public void setWiringService(WiringService wiringService){
		this.wiringService = wiringService;
	}

	protected final RegistryServiceContext getRegistryServiceContext (){
		return serviceRegistryContext;
	}
	
	protected LoggingService resolveLoggingService(){
		return new BroadcastLoggingService();
	}
	
	protected LocalizationService resolveLocalizationService(){
		return new CultureModelLocalizationService();
	}
	
	protected LoggingService getLoggingService(){
		return loggingService;
	}
	
	protected BootstrapEnvironment getContainer(){
		return this.environment;
	}
	
	protected abstract String getExecutionConfiguration(); 


	public void addActivatorDependencyResolver(ActivatorDependencyResolver listener) {
		resolvers.add(listener);
	}

	public void removeActivatorDependencyResolver(ActivatorDependencyResolver listener) {
		resolvers.remove(listener);
	}
	
	
	/**
	 * 
	 * @return the {@link BootstrapEnvironmentResolver} implementations to use.
	 */
	protected abstract BootstrapEnvironmentResolver bootstrapEnvironmentResolver();

	/**
	 * Start the environment 
	 */
	protected final void start(){


		StopWatch watch = StopWatch.start();
		
		// Phase 1 - default services
		
		// set Logging Service
		this.loggingService = this.resolveLoggingService();
		
		// Register Logging Service
		serviceRegistryContext.register(LoggingService.class, loggingService);
		
		// set default logger
		this.logger = new LogServiceDelegatorLogger(this.getClass().getName(), loggingService);
		
		
		serviceRegistryContext.register(LocalizationService.class, resolveLocalizationService());
		
		logger.info("Begin MiddleHeaven bootstrap");
		
		
		
		// set the environment resolver
		this.bootstrapEnvironmentResolver = this.bootstrapEnvironmentResolver();
		
		// Determine Bootstrap Service
		bootstrapService = new SimpleBootstrapService(this);
		
		bootstrapService.addListener(new BootstapListener(){

			@Override
			public void onBoostapEvent(BootstrapEvent event) {
				if (event.isBootdown()){
					wiringService.close();
					serviceRegistryContext.clear();
				}
			}
			
		});
		
		// Register Bootstrap Service
		serviceRegistryContext.register(BootstrapService.class, bootstrapService);
		serviceRegistryContext.register(WiringService.class, wiringService);
		
		final SetActivatorScanner scanner = new SetActivatorScanner();
		
		BootstrapContext context = new BootstrapContext(){

			public String getName(){
				return getExecutionConfiguration();
			}
			
			@Override
			public BootstrapContext addActivator(
					Class<? extends ServiceActivator> type) {
				scanner.addActivator(type);
				return this;
			}

			@Override
			public BootstrapContext removeActivator(Class<? extends ServiceActivator> type) {
				scanner.removeActivator(type);
				return this;
			}

			@Override
			public boolean contains(Class<? extends ServiceActivator> type) {
				return scanner.contains(type);
			}

			@Override
			public ProfilesBag getActiveProfiles() {
				return wiringService.getActiveProfiles();
			}

			@Override
			public ServiceContext getServiceContext() {
				return serviceRegistryContext;
			}

			@Override
			public PropertyManagers getPropertyManagers() {
				return propertyManagers;
			}
			
		};
		
		// Resolve Container
		
		// Phase 2 - enviroment
		
		this.environment = bootstrapEnvironmentResolver.resolveEnvironment(context);

		logger.info("Using environment : '{0}'", environment.getName());
		
		
		// Resolve FileSystem
		final FileContext fileSystem = environment.getFileContext();

		serviceRegistryContext.register(FileContextService.class, new FileContextService(){

			@Override
			public FileContext getFileContext() {
				return fileSystem;
			}
			
		});

		doBeforeStart(context);
		
		// apply profiles to wiring service
		logger.trace("Configuring active environment profiles");
		
		String profiles = (String) propertyManagers.getProperty("middleheaven.profiles.active");
		
		if (profiles != null) {
			String[] all = StringUtils.split(profiles, ',');
			wiringService.getActiveProfiles().add(all);
		}
		
		logger.debug("Register environment services");

		// activate services that can be overridden by the container or final environment	

		logger.debug("Scanning for extentions");

		List<BootstrapContainerExtention> extentions = new ArrayList<BootstrapContainerExtention>();
		
		readExtentions(extentions);

		// configurate container with chain of extentions
		BootstrapChain chain = new BootstrapChain(logger, extentions,environment);

		logger.debug("Configuring context");

		logger.debug("Inicialize service discovery");

		addActivatorScanner(scanner);
		
		// config any specific environment activators
		preConfig(context);

		// start bootstrap
		// any participant that wishes to add activators can do so now.
		bootstrapService.fireBootupStart();

		// configure using extentions
		chain.doChain(context); 

		// call configuration
		posConfig(context);

		// at this point all needed activators are collected.
		
		// scan and activate all services
		activate(); 
		
		// Phase 3 - inicilize modules
		//ApplicationModulesResolver modulesResolver = environment.getApplicationModulesResolver();
		
		
		//modulesResolver.resolveApplicationModules();
		
		//
		wiringService.refresh();

		doAfterStart(context);
		
		// inform the end of the bootstrap
		bootstrapService.fireBootupEnd();
		
		logger.info("MiddleHeaven bootstraped in {0} ms. " , watch.mark().milliseconds());
		
		logger.info("Starting environment");
		
		// start the environment. the ui is available.
		environment.start();

		
		
	}

	

	protected void activate(){
		final ScanActivatorScannerListener listener = new ScanActivatorScannerListener();
		
		for (ActivatorScanner scanner : scanners){
			scanner.addScannerListener(listener);
			scanner.scan();
		}
		
		final DependencyResolver dependencyResolver = new DependencyResolver(
				new LogServiceDelegatorLogger("dependencyResolver", loggingService)
		);
		
		dependencyResolver.resolve(activators, new ActivatorsStarter());


//
//		final Collection<BeanDependencyModel> dependencyModels = new HashSet<BeanDependencyModel>();
//
//		for (ActivatorScanner scanner : scanners){
//			doScan(scanner, dependencyModels, wiringService);
//		}
//
//		resolveDependencies(dependencyModels, wiringService);
//
//		scanEarly = true;

//		this.wiringService.refresh();
		
	}
	
	boolean scanEarly = false;

//	private void doScan(ActivatorScanner scanner, final Collection<BeanDependencyModel> dependencyModels, WiringService wiringService){
//	
//
//		scanner.addScannerListener(listener);
//		scanner.scan();
//
//		for (Class<? extends ServiceActivator> activatorType : listener.activatorTypes){
//			BeanDependencyModel model = new BeanDependencyModel(activatorType);
//
//			for (ActivatorDependencyResolver resolver : this.resolvers){
//
//				resolver.resolveDependency(activatorType, model);
//			}
//			dependencyModels.add(model);
//		}
//
//		if (scanEarly){
//			resolveDependencies(dependencyModels, wiringService);
//		}
//	}

	

	
	class ActivatorsStarter implements Starter<ServiceActivatorModel>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public List<ServiceActivatorModel> sort(Collection<ServiceActivatorModel> dependencies) {
			
			List<ServiceActivatorModel> list = new ArrayList<ServiceActivatorModel>(dependencies);

			Collections.sort(list, new Comparator<ServiceActivatorModel>(){

				@Override
				public int compare(ServiceActivatorModel a,
						ServiceActivatorModel b) {
					return a.getRequiredServices().size() - b.getRequiredServices().size();
				}

			});

			return list;
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void inicialize(ServiceActivatorModel model)
				throws InicializationNotResolvedException,
				InicializationNotPossibleException {
			// no-op
			
			model.activate(serviceRegistryContext);
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void inicializeWithProxy(
				ServiceActivatorModel dependableProperties)
				throws InicializationNotResolvedException,
				InicializationNotPossibleException {
			// no-op
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isRequired(ServiceActivatorModel dependency) {
			return true; // TODO remove method as activators are always required
		}

		
		
	}


	private class ScanActivatorScannerListener implements ActivatorScannerListener{

		@Override
		public void onActivatorFound(ActivatorScannerEvent event) {
			
			ServiceActivator activator = ClassIntrospector.of(event.getActivatorType()).newInstance();
			
			activators.add(new ServiceActivatorModel(activator));
		}

		@Override
		public void onActivatorLost(ActivatorScannerEvent event) {}

	}

	/**
	 * Romed an {@link ActivatorScanner} from the service.
	 * 
	 * @param scanner the {@link ActivatorScanner} to remove.
	 */
	public void removeActivatorScanner(ActivatorScanner scanner) {
		scanners.remove(scanner);

	}


	/**
	 * Add an {@link ActivatorScanner} to the service.
	 * 
	 * @param scanner the {@link ActivatorScanner} to add.
	 * @param wiringService 
	 */
	public void addActivatorScanner(ActivatorScanner scanner) {
		scanners.add(scanner); // TODO remove activators scanned by it ?
	}



	protected void readExtentions(List<BootstrapContainerExtention> extentions){

		ManagedFile file = this.getContainer().getFileContext().getAppConfigRepository().retrive("extensions.config");
		
		if (file.exists()) {
			Properties p = PropertiesFile.from(file).toProperties();
			
			
			String[] all = StringUtils.split(p.getProperty("extentions"), ",");
			
			for (String extention : all){
				extentions.add(Introspector.of(BootstrapContainerExtention.class).load(extention).newInstance());
			}
		}
		
	}

	protected void preConfig(BootstrapContext context) {
		// no-op
	}

	public void posConfig(BootstrapContext context){

	}

	private class SimpleBootstrapService implements BootstrapService{

		private AbstractBootstrap executionEnvironmentBootstrap;
		private Set<BootstapListener> listeners = new CopyOnWriteArraySet<BootstapListener>();

		public SimpleBootstrapService(AbstractBootstrap executionEnvironmentBootstrap) {
			this.executionEnvironmentBootstrap = executionEnvironmentBootstrap;
		}

		protected void fireBootupStart() {
			fire (new BootstrapEvent(true,true));
		}

		protected void fireBootupEnd() {
			fire (new BootstrapEvent(true,false));
		}

		protected void fireBootdownEnd() {
			fire (new BootstrapEvent(false,false));
		}

		protected void fireBootdownStart() {
			fire (new BootstrapEvent(false,true));
		}

		private void fire(BootstrapEvent event) {
			for (BootstapListener listener : listeners){
				listener.onBoostapEvent(event);
			}
		}


		@Override
		public void addListener(BootstapListener listener) {
			listeners.add(listener);
		}

		@Override
		public void removeListener(BootstapListener listener) {
			listeners.remove(listener);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void stop() {
			executionEnvironmentBootstrap.stop();
		}

	}

	public final void stop(){
		logger.info("Terminating Environment");
		bootstrapService.fireBootdownStart();
		doBeforeStop(context);

		serviceRegistryContext.unRegister(BootstrapService.class);
		serviceRegistryContext.unRegister(WiringService.class);
		
		// TODO unregister all

		doAfterStop(context);

		bootstrapService.fireBootdownEnd();
		logger.info("Environment terminated");
	}

	protected void doBeforeStart(BootstrapContext context){};
	protected void doAfterStart(BootstrapContext context){}; 
	protected void doBeforeStop(BootstrapContext context){};
	protected void doAfterStop(BootstrapContext context){}







}
