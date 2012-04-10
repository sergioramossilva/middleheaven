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
import org.middleheaven.core.bootstrap.activation.FileActivatorScanner;
import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.SetActivatorScanner;
import org.middleheaven.core.dependency.DependencyResolver;
import org.middleheaven.core.dependency.InicializationNotPossibleException;
import org.middleheaven.core.dependency.InicializationNotResolvedException;
import org.middleheaven.core.dependency.Starter;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.RegistryServiceContext;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.InstanceWiringItem;
import org.middleheaven.core.wiring.ProfilesBag;
import org.middleheaven.core.wiring.StandardWiringService;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.global.text.LocalizationServiceActivator;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.PropertiesFile;
import org.middleheaven.logging.BroadcastLoggingService;
import org.middleheaven.logging.LogServiceDelegatorLogger;
import org.middleheaven.logging.Logger;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.util.StopWatch;
import org.middleheaven.util.StringUtils;

/**
 * This is the entry point for all applications
 * Subclasses of <code>ExecutionEnvironmentBootstrap</code> implement
 * bootstrap in different execution environments and allow
 * for the application's execution to be environment independent.
 * 
 *
 */
public abstract class ExecutionEnvironmentBootstrap {

	private final RegistryServiceContext serviceRegistryContext = new RegistryServiceContext();
	private final List<ServiceActivatorModel> activators = new ArrayList<ServiceActivatorModel>();
	private final Set<ActivatorScanner> scanners = new HashSet<ActivatorScanner>();
	private final Set<ActivatorDependencyResolver> resolvers = new CopyOnWriteArraySet<ActivatorDependencyResolver>();
	private WiringService wiringService = new StandardWiringService();
	private SimpleBootstrapService bootstrapService;
	private LoggingService loggingService;
	private Logger logger;
	private BootstrapContainer container;

	
	
	public ExecutionEnvironmentBootstrap (){
		resolvers.add(new AnnotationBasedDependencyResolver());
	}

	public WiringService getWiringService(){
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
	
	public LoggingService getLoggingService(){
		return loggingService;
	}
	
	
	public BootstrapContainer getContainer(){
		return this.container;
	}
	
	protected abstract BootstrapContainer resolveContainer();
	
	protected abstract String getExecutionConfiguration(); 


	protected  ActivatorScanner resolverActivatorScanner(ManagedFile appClasspathRepository){
		return new FileActivatorScanner(appClasspathRepository, ".jar$");
	}

	public void addActivatorDependencyResolver(ActivatorDependencyResolver listener) {
		resolvers.add(listener);
	}

	public void removeActivatorDependencyResolver(ActivatorDependencyResolver listener) {
		resolvers.remove(listener);
	}

	/**
	 * Start the environment 
	 */
	protected final void start(){

		StopWatch watch = StopWatch.start();
		
		// Determine Logging Service
		this.loggingService = this.resolveLoggingService();

		this.logger = new LogServiceDelegatorLogger(this.getClass().getName(), loggingService);
		
		logger.info("Inicializing Environment");

		// Register Logging Service
		serviceRegistryContext.register(LoggingService.class, loggingService);
		
		
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
		
		// Resolve Container
		
		this.container = resolveContainer();

		logger.info("Using container : '{0}'", container.getContainerName());
		
		
		// Resolve FileSystem
		final ContainerFileSystem fileSystem = container.getFileSystem();

		
		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(ContainerFileSystem.class).inSharedScope().toInstance(fileSystem);
			}
			
		});

		doBeforeStart();
		
		
		logger.trace("Configuring active environment profiles");
		
		String profiles = (String) wiringService.getPropertyManagers().getProperty("middleheaven.profiles.active");
		
		if (profiles != null) {
			String[] all = StringUtils.split(profiles, ',');
			wiringService.getActiveProfiles().add(all);
		}
		
		logger.debug("Register bootstrap services");

		// activate services that can be overridden by the container or final environment	

		logger.debug("Scanning for extentions");

		List<BootstrapContainerExtention> extentions = new ArrayList<BootstrapContainerExtention>();
		
		readExtentions(extentions);

		logger.debug("Inicialize service discovery");
		// file aware scanner
		ActivatorScanner fileScanner = this.resolverActivatorScanner(fileSystem.getAppClasspathRepository());
		
		addActivatorScanner(fileScanner);

		
		final SetActivatorScanner scanner = new SetActivatorScanner();
		
		scanner.addActivator(LocalizationServiceActivator.class);
		
		addActivatorScanner(scanner);
		
		// configurate container with chain of extentions
		BootstrapChain chain = new BootstrapChain(logger, extentions,container);

		ExecutionContext context = new ExecutionContext(){

			public String getName(){
				return getExecutionConfiguration();
			}
			
			@Override
			public ExecutionContext addActivator(
					Class<? extends ServiceActivator> type) {
				scanner.addActivator(type);
				return this;
			}

			@Override
			public ExecutionContext removeActivator(Class<? extends ServiceActivator> type) {
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
			
		};
			
		logger.debug("Configuring context");
			
		// config any specifica environment activators
		preConfig(context);

		// start bootstrap
		// any participant that wishes to add activators can do so now.
		bootstrapService.fireBootupStart();

		// configure using extentions
		chain.doChain(context); 

		// call configuration
		posConfig(context);

		// at this point all needed activators are collected.
		
		// scan and activate all
		activate(); 
		
		wiringService.refresh();

		doAfterStart();

		// start the container.
		container.start();


		// inform the end of the bootstrap
		bootstrapService.fireBootupEnd();
		
		logger.info("Environment inicialized in {0} ms. " , watch.mark().milliseconds());
		
	}

	

	protected void activate(){
		final ScanActivatorScannerListener listener = new ScanActivatorScannerListener();
		
		for (ActivatorScanner scanner : scanners){
			scanner.addScannerListener(listener);
			scanner.scan();
		}
		
		final DependencyResolver dependencyResolver = new DependencyResolver(new LogServiceDelegatorLogger("dependencyResolver", loggingService));
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

		ManagedFile file = this.getContainer().getFileSystem().getAppConfigRepository().retrive("extensions.config");
		
		if (file.exists()) {
			Properties p = PropertiesFile.from(file).toProperties();
			
			
			String[] all = StringUtils.split(p.getProperty("extentions"), ",");
			
			for (String extention : all){
				extentions.add(Introspector.of(BootstrapContainerExtention.class).load(extention).newInstance());
			}
		}
		
	}

	protected void preConfig(ExecutionContext context) {
		// no-op
	}

	public void posConfig(ExecutionContext context){

	}

	private class SimpleBootstrapService implements BootstrapService{

		private ExecutionEnvironmentBootstrap executionEnvironmentBootstrap;
		private Set<BootstapListener> listeners = new CopyOnWriteArraySet<BootstapListener>();

		public SimpleBootstrapService(ExecutionEnvironmentBootstrap executionEnvironmentBootstrap) {
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

		@Override
		public ExecutionEnvironmentBootstrap getEnvironmentBootstrap() {
			return executionEnvironmentBootstrap;
		}

	}

	public final void stop(){
		logger.info("Terminating Environment");
		bootstrapService.fireBootdownStart();
		doBeforeStop();

		serviceRegistryContext.unRegister(BootstrapService.class);
		serviceRegistryContext.unRegister(WiringService.class);

		doAfterStop();

		bootstrapService.fireBootdownEnd();
		logger.info("Environment terminated");
	}

	protected void doBeforeStart(){};
	protected void doAfterStart(){}; 
	protected void doBeforeStop(){};
	protected void doAfterStop(){}







}
