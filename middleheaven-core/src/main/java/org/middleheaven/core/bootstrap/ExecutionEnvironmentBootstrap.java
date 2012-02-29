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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.dependency.DependencyResolver;
import org.middleheaven.core.dependency.InicializationNotPossibleException;
import org.middleheaven.core.dependency.InicializationNotResolvedException;
import org.middleheaven.core.dependency.Starter;
import org.middleheaven.core.services.RegistryServiceContext;
import org.middleheaven.core.wiring.BeanModel;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.BindingException;
import org.middleheaven.core.wiring.ObjectPool;
import org.middleheaven.core.wiring.ObjectPoolEvent;
import org.middleheaven.core.wiring.ObjectPoolListener;
import org.middleheaven.core.wiring.StandardWiringService;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.ActivatorDependencyResolver;
import org.middleheaven.core.wiring.activation.ActivatorScanner;
import org.middleheaven.core.wiring.activation.ActivatorScannerEvent;
import org.middleheaven.core.wiring.activation.ActivatorScannerListener;
import org.middleheaven.core.wiring.activation.AnnotationBasedDependencyResolver;
import org.middleheaven.core.wiring.activation.FileActivatorScanner;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.global.atlas.AtlasActivator;
import org.middleheaven.global.text.LocalizationServiceActivator;
import org.middleheaven.io.repository.FileRepositoryActivator;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.CompositeLogBook;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingActivator;
import org.middleheaven.util.StopWatch;
import org.middleheaven.util.collections.ParamsMap;

/**
 * This is the entry point for all applications
 * Subclasses of <code>ExecutionEnvironmentBootstrap</code> implement
 * bootstrap in different execution environments and allow
 * for the application's execution to be environment independent.
 * 
 *
 */
public abstract class ExecutionEnvironmentBootstrap {


	private final List<Activator> activators = new ArrayList<Activator>();
	private final Set<ActivatorScanner> scanners = new HashSet<ActivatorScanner>();
	private final Set<ActivatorDependencyResolver> resolvers = new CopyOnWriteArraySet<ActivatorDependencyResolver>();

	private WiringService wiringService = new StandardWiringService();

	private CompositeLogBook containerLogBook = new CompositeLogBook("executionLog");

	private SimpleBootstrapService bootstrapService;
	private RegistryServiceContext serviceRegistryContext;

	private LogBook log;

	private BootstrapContainer container;

	public ExecutionEnvironmentBootstrap (){
		resolvers.add(new AnnotationBasedDependencyResolver());
	}


	public  BootstrapContainer getContainer(){
		return container;
	}

	public WiringService getWiringService(){
		return this.wiringService;
	}

	public void setWiringService(WiringService wiringService){
		this.wiringService = wiringService;
	}


	protected abstract BootstrapContainer resolveContainer();


	public void addActivatorDependencyResolver(ActivatorDependencyResolver listener) {
		resolvers.add(listener);
	}

	public void removeActivatorDependencyResolver(ActivatorDependencyResolver listener) {
		resolvers.remove(listener);
	}


	/**
	 * Start the environment 
	 */
	public final void start(LogBook log){


		StopWatch watch = StopWatch.start();
		
		this.log= log;

		log.info("Inicializing Environment");

		serviceRegistryContext = new RegistryServiceContext(log);
		
		bootstrapService = new SimpleBootstrapService(this);
		
		final ObjectPool objectPool = wiringService.getObjectPool();
		
		objectPool.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(WiringService.class).in(Service.class).toInstance(wiringService);
				binder.bind(BootstrapService.class).in(Service.class).toInstance(bootstrapService);
			}

		});
		
		this.container = resolveContainer();

		final ContainerFileSystem fileSystem = container.getFileSystem();

		log.info("Using container : '{0}'", container.getContainerName());

		doBeforeStart();

		log.debug("Register bootstrap services");

		this.containerLogBook.addLogBook(log);


		objectPool.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(ContainerFileSystem.class).in(Service.class).toInstance(fileSystem);
			}

		});

		// force initialization
		objectPool.getInstance(WiringService.class);
		objectPool.getInstance(BootstrapService.class);
		objectPool.getInstance(ContainerFileSystem.class);
		
		// set scanner
		final SetActivatorScanner scanner = new SetActivatorScanner()
		.addActivator(FileRepositoryActivator.class)
		.addActivator(AtlasActivator.class)
		.addActivator(LoggingActivator.class)
		.addActivator(LocalizationServiceActivator.class);


		BootstrapContext context = new BootstrapContext(){

			@Override
			public BootstrapContext addActivator(Class<? extends Activator> activatorType) {
				scanner.addActivator(activatorType);
				return this;
			}

			@Override
			public BootstrapContext removeActivator(Class<? extends Activator> activatorType) {
				scanner.removeActivator(activatorType);
				return this;
			}

			@Override
			public boolean contains(Class<? extends Activator> activatorType) {
				return scanner.contains(activatorType);
			}

		};

		// activate services that can be overridden by the container or final environment	


		preConfig(context);

		bootstrapService.fireBootupStart();

		log.debug("Inicialize service discovery engines");


		// file aware scanner
		ActivatorScanner fileScanner = new FileActivatorScanner(fileSystem.getAppClasspathRepository(), ".jar$");
		addActivatorScanner(fileScanner);

		log.debug("Scanning for extentions");

		List<BootstrapContainerExtention> extentions = new ArrayList<BootstrapContainerExtention>();
		readExtentions(extentions);

		// configurate container with chain of extentions
		BootstrapChain chain = new BootstrapChain(extentions,container);

		chain.doChain(context);

		// call configuration
		posConfig(context);


		addActivatorScanner(scanner);

		// scan and activate all
		scan(wiringService); 

		doAfterStart();

		container.start();

		log.info("Environment inicialized in {0}. " , watch.mark());
		bootstrapService.fireBootupEnd();
	}


	protected void scan(WiringService wiringService){

		final String bookName = this.getClass().getName();
		
		wiringService.getObjectPool().addObjectCycleListener(new ObjectPoolListener() {
			
			@Override
			public void onObjectRemoved(ObjectPoolEvent objectPoolEvent) {
				
				if (objectPoolEvent.getObject() instanceof LogBook){
					containerLogBook.addLogBook((LogBook) objectPoolEvent.getObject());
				}
	
			}
			
			@Override
			public void onObjectAdded(ObjectPoolEvent objectPoolEvent) {
				//no-op
			}
		});

		final Collection<BeanModel> dependencyModels = new HashSet<BeanModel>();

		for (ActivatorScanner scanner : scanners){
			doScan(scanner, dependencyModels, wiringService);
		}

		resolveDependencies(dependencyModels, wiringService);

		scanEarly = true;

	}

	boolean scanEarly = false;

	private void resolveDependencies(Collection<BeanModel> dependencyModels, WiringService wiringService){

		final DependencyResolver dependencyResolver = new DependencyResolver(containerLogBook);
		dependencyResolver.resolve(dependencyModels, new StarterMy(wiringService));

	

	}

	class StarterMy implements Starter<BeanModel>{

		private WiringService wiringService;

		public StarterMy(WiringService wiringService){
			this.wiringService = wiringService;
		}

		private Activator startActivator(final BeanModel model){

			wiringService.getObjectPool().addConfiguration(new BindConfiguration(){

				@Override
				public void configure(Binder binder) {

					Class c = model.getBeanClass();
					binder.bind(model.getBeanClass()).to(c).in(Shared.class).withParams(model.getParams());


				}

			});

			Activator activator = (Activator) wiringService.getObjectPool().getInstance(model.getBeanClass());

			return activator;
		}

		@Override
		public void inicialize(BeanModel model) 
				throws InicializationNotResolvedException, InicializationNotPossibleException {

			try{
				Activator activator = startActivator(model); 

				// add activator to context for future inactivation
				activators.add(activator);
			}catch (BindingException e){
				throw new InicializationNotResolvedException();
			}catch (RuntimeException e){
				throw new InicializationNotPossibleException(e);
			}
		}

		@Override
		public void inicializeWithProxy(BeanModel model)
				throws InicializationNotResolvedException,InicializationNotPossibleException {

			try{
				// try for the last time
				Activator activator = startActivator(model); 

				// add activator to context for future inactivation
				activators.add(activator);
			}catch (RuntimeException e){
				// will never work
				throw new InicializationNotPossibleException(e);
			}
		}

		@Override
		public List<BeanModel> sort(Collection<BeanModel> dependencies) {

			List<BeanModel> list = new ArrayList<BeanModel>(dependencies);

			Collections.sort(list, new Comparator<BeanModel>(){

				@Override
				public int compare(BeanModel a,
						BeanModel b) {
					return a.getAfterPoints().size() - b.getAfterPoints().size();
				}

			});

			return list;
		}


		@Override
		public boolean isRequired(BeanModel dependency) {
			return dependency.isRequired();
		}


	}


	private static class ScanActivatorScannerListener implements ActivatorScannerListener{

		public Collection<Class<? extends Activator>> activatorTypes = new LinkedList<Class<? extends Activator>>();

		@Override
		public void onActivatorFound(ActivatorScannerEvent event) {
			activatorTypes.add(event.getActivatorType());
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
		scanners.add(scanner);

		//		if(scanEarly){
		//			doScan(scanner, new ArrayList<BeanModel>(1), wiringService);
		//		}
	}


	private void doScan(ActivatorScanner scanner, final Collection<BeanModel> dependencyModels, WiringService wiringService){
		final ScanActivatorScannerListener listener = new ScanActivatorScannerListener();


		scanner.addScannerListener(listener);
		scanner.scan(wiringService);

		for (Class<? extends Activator> activatorType : listener.activatorTypes){
			BeanModel model = new BeanModel(activatorType);

			for (ActivatorDependencyResolver resolver : this.resolvers){

				resolver.resolveDependency(activatorType, model);
			}
			dependencyModels.add(model);
		}

		if (scanEarly){
			resolveDependencies(dependencyModels, wiringService);
		}
	}

	protected void readExtentions(List<BootstrapContainerExtention> extentions){

	}

	protected void preConfig(BootstrapContext contex) {
		// no-op
	}

	public void posConfig(BootstrapContext context){

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
		log.info("Terminating Environment");
		bootstrapService.fireBootdownStart();
		doBeforeStop();

		serviceRegistryContext.unRegister(BootstrapService.class);
		serviceRegistryContext.unRegister(WiringService.class);

		doAfterStop();

		bootstrapService.fireBootdownEnd();
		log.info("Environment terminated");
	}

	protected void doBeforeStart(){};
	protected void doAfterStart(){}; 
	protected void doBeforeStop(){};
	protected void doAfterStop(){}; 





}
