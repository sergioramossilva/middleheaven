package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.dependency.DependencyResolver;
import org.middleheaven.core.dependency.InicializationNotPossibleException;
import org.middleheaven.core.dependency.InicializationNotResolvedException;
import org.middleheaven.core.dependency.Starter;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.ActivatorDependencyResolver;
import org.middleheaven.core.wiring.activation.ActivatorScanner;
import org.middleheaven.core.wiring.activation.ActivatorScannerEvent;
import org.middleheaven.core.wiring.activation.ActivatorScannerListener;
import org.middleheaven.core.wiring.activation.AnnotationBasedDependencyResolver;
import org.middleheaven.core.wiring.activation.PublishPoint;
import org.middleheaven.core.wiring.activation.UnitActivatorDepedencyModel;
import org.middleheaven.core.wiring.annotations.Default;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.core.wiring.service.ServiceScope;
import org.middleheaven.logging.CompositeLogBook;
import org.middleheaven.logging.ListLogBookWriter;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.logging.WritableLogBook;

@Service
public class DefaultWiringService implements WiringService{


	private final PropertyResolver<Object> propertyResolver = new PropertyResolver<Object>();
	private final Set<ActivatorDependencyResolver> resolvers = new CopyOnWriteArraySet<ActivatorDependencyResolver>();

	private final List<Activator> activators = new ArrayList<Activator>();

	private final Set<ActivatorScanner> scanners = new HashSet<ActivatorScanner>();
	private final DefaultObjectPool wiringContext = new DefaultObjectPool();
	private final BinderImpl binder = new BinderImpl();
	private final List<WiringInterceptor> interceptors = new CopyOnWriteArrayList<WiringInterceptor>();
	private final Map<String,Class<? extends ScopePool>> scopes = new TreeMap<String,Class<? extends ScopePool>>();
	private final Map<String, ScopePool> scopePools = new TreeMap<String,ScopePool>();
	private CompositeLogBook containerLogBook = new CompositeLogBook("wiringLog");

	public DefaultWiringService(LogBook containerLogBook){
		
		this.containerLogBook.addLogBook(containerLogBook);
		
		scopes.put(Shared.class.getName(), SharedScope.class);
		scopes.put(Default.class.getName(), DefaultScope.class);
		scopes.put(Service.class.getName(), ServiceScope.class);

		DefaultScope defaultScope = new DefaultScope();
		SharedScope sharedScope = new SharedScope();

		scopePools.put(DefaultScope.class.getName(),defaultScope);
		scopePools.put(SharedScope.class.getName(),sharedScope);

		binder.bind(SharedScope.class).in(Shared.class).toInstance(sharedScope);
		binder.bind(DefaultScope.class).in(Shared.class).toInstance(defaultScope);
		binder.bind(ServiceScope.class).in(Shared.class);

		// add connector

		if (ClassIntrospector.isInClasspath("javax.annotation.Resource") && 
				ClassIntrospector.isInClasspath("org.middleheaven.core.wiring.connectors.JavaEE5InjectonConnector")	){
			this.addConnector((WiringConnector) ClassIntrospector.loadFrom("org.middleheaven.core.wiring.connectors.JavaEE5InjectonConnector").newInstance());
		}

	}


	@Override
	public ObjectPool getObjectPool() {
		return wiringContext;
	}

	private Map<Key , Binding> bindings = new HashMap<Key , Binding>();

	private class BinderImpl implements Binder,EditableBinder,ConnectableBinder{

		Queue<Key> stack = new LinkedList<Key>();
		Map<Key, CyclicProxy> cyclicProxies = new HashMap<Key, CyclicProxy>();
		Set<WiringModelReader> parsers= new CopyOnWriteArraySet<WiringModelReader>();

		public BinderImpl(){
			parsers.add(new DefaultWiringModelParser());

		}

		@Override
		public <T> BindingBuilder<T> bind(Class<T> type) {
			return new BindingBuilder<T>(this, type);
		}

		@Override
		public <S extends ScopePool> void bindScope(Class<? extends Annotation> annotation, Class<S> scopeClass) {
			scopes.put(annotation.getName(),scopeClass);
		}


		@Override
		public <T> PropertyBindingBuilder<T> bindProperty(Class<T> type) {
			Binding binding = new Binding();
			binding.setStartType(type);
			binding.setResolver(propertyResolver);

			PropertyBindingBuilder<T> b = new PropertyBindingBuilder<T>(this,binding);
			binder.addBinding(binding);

			return b;
		}

		@Override
		public void addBinding(Binding binding) {
			bindings.put(binding.getKey(), binding);
		}

		public ScoopingModel getScoopingModel(Object obj) {
			ScoopingModel model = new ScoopingModel();

			if (String.class.isInstance(obj) || Number.class.isInstance(obj)){
				return model;
			}

			for (WiringModelReader reader : this.parsers){
				reader.readScoopingModel(obj, model);
			}
			return model;
		}

		@Override
		public WiringModel getWiringModel(Class<?> type) {
			WiringModel model = new WiringModel();

			if (String.class.isAssignableFrom(type) || Number.class.isAssignableFrom(type)){
				return model;
			}

			for (WiringModelReader reader : this.parsers){
				reader.readWiringModel(type, model);
			}
			return model;
		}

		@Override
		public void addWiringModelParser(WiringModelReader parser) {
			parsers.add(parser);
		}

		@Override
		public void removeWiringModelParser(WiringModelReader parser) {
			parsers.remove(parser);
		}

		@Override
		public <T> T getInstance( WiringSpecification<T> query ){
			Key key = Key.keyFor(query.getContract(), query.getSpecifications());

			Binding binding = bindings.get(key);
			if (binding==null){
				// is binding is not found try to gess one automaticly

				// if its a concrete classe create a binding now
				if (!query.getContract().isAnnotation() && !query.getContract().isInterface() && !Modifier.isAbstract(query.getContract().getModifiers())){
					this.bind(query.getContract()).to(query.getContract());
					return getInstance(query); // repeat search
				}

				// try understand from wich scope could be readed
				Set<Annotation> annotations = Introspector.of(query.getContract()).inspect().annotations().retrive();

				if (annotations.isEmpty()){
					annotations.addAll(query.getSpecifications());
				}

				// find a scope annotation
				Annotation found=null;
				for (Annotation a : annotations){
					if(scopes.containsKey(a.annotationType().getName())){
						if ( found !=null){
							// already found one previouslly
							throw new BindingException("To many auto-binding options");
						}
						found = a; // do not break
					}
				}

				if (found!=null){
					// found a scope
					// add a biding
					new BindingBuilder<T>(this,query.getContract()).in(found.annotationType());
					return getInstance(query); // repeat search
				} else {
					if (query.isRequired()){
						throw new BindingNotFoundException(query.getContract());
					} else {
						return null;
					}
				}

			}

			// binding was found
			// resolve target
			if (stack.contains(key)){
				// cyclic reference
				// return proxy
				CyclicProxy proxy = cyclicProxies.get(key);
				if (proxy==null){
					proxy = new CyclicProxy();
					cyclicProxies.put(key, proxy);
				}
				return  Introspector.of(query.getContract()).newProxyInstance(proxy);

			} else {
				stack.offer(key);
				try{
					// resolve scope 
					ScopePool scopePool = getScopePool(binding);


					// get resolver
					Resolver<T> resolver = binding.getResolver();

					if (resolver == null && !binding.getAbstractType().isInterface()){
						resolver =  FactoryResolver.instanceFor(binding.getAbstractType(),this);
					}

					if (resolver != null) {
						// attach interceptors
						final InterceptorResolver<T> interceptorResolver = new InterceptorResolver<T>(interceptors,resolver);
						T obj = scopePool.getInScope(query, interceptorResolver);

						//wireMembers(obj);

						stack.remove(key);
						// check if there is any proxy to complete
						CyclicProxy proxy = cyclicProxies.get(key);
						if (proxy!=null){
							proxy.setRealObject(obj);
						}
						return  obj;
					} else {
						return null;
					}
				} catch (RuntimeException e){
					stack.remove(key);
					throw e;
				}
			}

		}

		public void wireMembers(Object obj) {
			WiringModel model = this.getWiringModel(obj.getClass());

			for (AfterWiringPoint a : model.getAfterPoints()){
				obj = a.writeAtPoint(binder, obj);
			}
		}

		@Override
		public void removeBinding(Binding binding) {
			bindings.remove(binding.getKey());

		}


		public ScopePool getScopePool(Binding binding) {
			Class<? extends ScopePool> scopeClass;
			if (binding.getScope()==null){
				scopeClass = scopes.get(Default.class.getName());
			} else {
				scopeClass = scopes.get(binding.getScope().getName());
				if (scopeClass==null){
					throw new BindingNotFoundException(binding.getScope());
				}
			}

			// find scope class
			ScopePool scopePool = scopePools.get(scopeClass.getName());
			if (scopePool==null){
				// the pool is also injectable
				scopePool = getInstance(WiringSpecification.search(scopeClass));
				scopePools.put(scopeClass.getName(), scopePool);
			}

			return scopePool;
		}

		@Override
		public void addInterceptor(WiringInterceptor interceptor) {
			interceptors.add(interceptor);
		}

		@Override
		public void removeInterceptor(WiringInterceptor interceptor) {
			interceptors.remove(interceptor);
		}


	}

	private class DefaultObjectPool implements ObjectPool{

		private DefaultObjectPool(){
			resolvers.add(new AnnotationBasedDependencyResolver());
		}

		@Override
		public ObjectPool addConfiguration(BindConfiguration... modules) {
			for (BindConfiguration m : modules){
				m.configure(binder);
			}
			return this;
		}

		@Override
		public <T> T getInstance(Class<T> type) {
			return binder.getInstance(WiringSpecification.search(type));
		}

		@Override
		public <T> T getInstance(Class<T> type, Map<String, String> params) {
			return binder.getInstance(WiringSpecification.search(type, params));
		}

		@Override
		public void wireMembers(Object obj) {
			binder.wireMembers(obj);
		}

	}




	public void addActivatorDependencyResolver(ActivatorDependencyResolver listener) {
		resolvers.add(listener);
	}

	public void removeActivatorDependencyResolver(ActivatorDependencyResolver listener) {
		resolvers.remove(listener);
	}

	@Override
	public void addConnector(WiringConnector... connectors) {
		for (WiringConnector c : connectors){
			c.connect(binder);
		}
	}

	boolean scanEarly = false;

	@Override
	public void removeActivatorScanner(ActivatorScanner scanner) {
		scanners.remove(scanner);

	}

	@Override
	public void addActivatorScanner(ActivatorScanner scanner) {
		scanners.add(scanner);

		if(scanEarly){
			doScan(scanner, new ArrayList<UnitActivatorDepedencyModel>(1));
		}
	}

	public void scan(){

		final Collection<UnitActivatorDepedencyModel> dependencyModels = new HashSet<UnitActivatorDepedencyModel>();

		for (ActivatorScanner scanner : scanners){
			doScan(scanner, dependencyModels);
		}

		resolveDependencies(dependencyModels);

		scanEarly = true;

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

	private void doScan(ActivatorScanner scanner, final Collection<UnitActivatorDepedencyModel> dependencyModels){
		final ScanActivatorScannerListener listener = new ScanActivatorScannerListener();


		scanner.addScannerListener(listener);
		scanner.scan(this);

		for (Class<? extends Activator> activatorType : listener.activatorTypes){
			UnitActivatorDepedencyModel model = new UnitActivatorDepedencyModel(activatorType);

			for (ActivatorDependencyResolver resolver : this.resolvers){

				resolver.resolveDependency(activatorType, model);
			}
			dependencyModels.add(model);
		}

		if (scanEarly){
			resolveDependencies(dependencyModels);
		}
	}



	private void resolveDependencies(Collection<UnitActivatorDepedencyModel> dependencyModels){
		

		final DependencyResolver dependencyResolver = new DependencyResolver(containerLogBook);
		dependencyResolver.resolve(dependencyModels, new StarterMy());

		final LogBook book = this.getObjectPool().getInstance(LoggingService.class).getLogBook(this.getClass().getName());
	
		
		containerLogBook.addLogBook(book);
		
	
	}



	class StarterMy implements Starter<UnitActivatorDepedencyModel>{



		public StarterMy(){}


		private Activator startActivator(UnitActivatorDepedencyModel model){
			Activator activator = model.getProducingWiringPoint().produceWith(binder);

			// fill requirements

			for (AfterWiringPoint a : model.getAfterPoints()){
				a.writeAtPoint(binder, activator);
			}

			// activate
			activator.activate(new ActivationContext(){});

			// publish services 
			for (PublishPoint pp : model.getPublishPoints()){
				Object object = pp.getObject(activator);

				if (object !=null){
					ScoopingModel scoopingModel = binder.getScoopingModel(object);
					scoopingModel.addParams(pp.getParams());

					scoopingModel.addToScope(binder,object);
				}

			}

			return activator;
		}

		@Override
		public void inicialize(UnitActivatorDepedencyModel model) 
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
		public void inicializeWithProxy(UnitActivatorDepedencyModel model)
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
		public List<UnitActivatorDepedencyModel> sort(Collection<UnitActivatorDepedencyModel> dependencies) {

			List<UnitActivatorDepedencyModel> list = new ArrayList<UnitActivatorDepedencyModel>(dependencies);

			Collections.sort(list, new Comparator<UnitActivatorDepedencyModel>(){

				@Override
				public int compare(UnitActivatorDepedencyModel a,
						UnitActivatorDepedencyModel b) {
					return a.getAfterPoints().size() - b.getAfterPoints().size();
				}

			});

			return list;
		}


		@Override
		public boolean isRequired(UnitActivatorDepedencyModel dependency) {
			return dependency.isRequired();
		}


	}



}