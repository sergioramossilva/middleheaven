package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.Binding.BindingScopeListener;
import org.middleheaven.core.wiring.annotations.Default;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.core.wiring.service.ServiceScope;
import org.middleheaven.events.EventListenersSet;
import org.middleheaven.util.collections.ClassMap;

@Service
public class StandardWiringService implements WiringService {

	private final PropertyResolver<Object> propertyResolver = new PropertyResolver<Object>();

	private final StandardObjectPool objectPool = new StandardObjectPool();
	private final BinderImpl binder = new BinderImpl();
	private final List<WiringInterceptor> interceptors = new CopyOnWriteArrayList<WiringInterceptor>();
	private final Map<String, Class<? extends ScopePool>> scopes = new TreeMap<String, Class<? extends ScopePool>>();
	private final Map<String, ScopePool> scopePools = new TreeMap<String, ScopePool>();

	public StandardWiringService() {

		scopes.put(Shared.class.getName(), SharedScope.class);
		scopes.put(Default.class.getName(), NewInstanceScope.class);
		scopes.put(Service.class.getName(), ServiceScope.class);

		NewInstanceScope newInstanceScope = new NewInstanceScope();
		SharedScope sharedScope = new SharedScope();

		// register scopes
		scopePools.put(NewInstanceScope.class.getName(), newInstanceScope);
		scopePools.put(SharedScope.class.getName(), sharedScope);

		// register scopes in shared scope.
		binder.bind(SharedScope.class).in(Shared.class).toInstance(sharedScope);
		binder.bind(NewInstanceScope.class).in(Shared.class)
				.toInstance(newInstanceScope);
		binder.bind(ServiceScope.class).in(Shared.class);
		binder.bind(ObjectPool.class).in(Shared.class).toInstance(objectPool);

		// add connector

		if (ClassIntrospector.isInClasspath("javax.annotation.Resource")
				&& ClassIntrospector
						.isInClasspath("org.middleheaven.core.wiring.connectors.JavaEE5InjectonConnector")) {
			this.addConnector((WiringConnector) ClassIntrospector
					.loadFrom(
							"org.middleheaven.core.wiring.connectors.JavaEE5InjectonConnector")
					.newInstance());
		}

	}

	@Override
	public ObjectPool getObjectPool() {
		return objectPool;
	}

	private BindingMap bindingMap = new BindingMap();

	private class BinderImpl implements Binder, EditableBinder,
			ConnectableBinder, BindingScopeListener {

		Queue<Key> stack = new LinkedList<Key>();
		Map<Key, CyclicProxy> cyclicProxies = new HashMap<Key, CyclicProxy>();
		Set<WiringModelReader> parsers = new CopyOnWriteArraySet<WiringModelReader>();

		public BinderImpl() {
			parsers.add(new DefaultWiringModelParser());

		}

		@Override
		public <T> BindingBuilder<T> bind(Class<T> type) {
			return new BindingBuilder<T>(this, type);
		}

		@Override
		public <S extends ScopePool> void bindScope(
				Class<? extends Annotation> annotation, Class<S> scopeClass) {
			scopes.put(annotation.getName(), scopeClass);
		}

		@Override
		public <T> PropertyBindingBuilder<T> bindProperty(Class<T> type) {
			Binding binding = new Binding();
			binding.setAbstractType(type);
			binding.setResolver(propertyResolver);

			PropertyBindingBuilder<T> b = new PropertyBindingBuilder<T>(this,
					binding);
			binder.addBinding(binding);

			return b;
		}

		@Override
		public void addBinding(Binding binding) {
			bindingMap.add(binding);

			// if (binding.getScope() != null){
			// onScopeChange(binding);
			// } else {
			// binding.addListeners(this);
			// }

		}

		@Override
		public void onScopeChange(Binding binding) {

			WiringSpecification spec = WiringSpecification.search(
					binding.getAbstractType(), binding.getParams());

			final ScopePool scopePool = binder.getScopePool(binding);
			// Object o = scopePool.getInScope(spec, NullResolver.instance());
			// if (o != null){
			// Log.onBookFor(this.getClass()).warn("Service {0} is beeing scoped more than once",binding.getAbstractType().getName());
			// }
			scopePool.add(spec, binding.getResolver().resolve(spec));

		}

		ClassMap<BeanModel> models = new ClassMap<BeanModel>();

		public BeanModel getBeanModel(Object object) {
			return getBeanModel(object.getClass());
		}

		@Override
		public BeanModel getBeanModel(Class<?> type) {

			BeanModel model = models.get(type);

			if (model == null) {
				model = new BeanModel(type);

				models.put(type, model);

				if (String.class.isAssignableFrom(type)
						|| Number.class.isAssignableFrom(type)) {
					return model;
				}

				for (WiringModelReader reader : this.parsers) {
					reader.readBeanModel(type, model);
				}
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
		public <T> T getInstance(WiringSpecification<T> query) {
			Key key = Key.keyFor(query.getContract(), query.getParams());

			Binding binding = bindingMap.findNearest(query);
			if (binding == null) {
				// is binding is not found try to guess one automatically

				// if its a concrete class create an auto-binding for it
				if (!query.getContract().isAnnotation()
						&& !query.getContract().isInterface()
						&& !Modifier.isAbstract(query.getContract()
								.getModifiers())) {

					BeanModel model = this.getBeanModel(query.getContract());

					binding = new Binding();

					binding.addParams(model.getParams());
					binding.setTargetScope(Shared.class);

					binding.setAbstractType(query.getContract());

					binding.setResolver(FactoryResolver.instanceFor(
							query.getContract(), binder));

					this.addBinding(binding);

					return getInstance(query); // repeat search recursively
				}

				// try understand from which scope could be read
				Set<Annotation> annotations = Introspector
						.of(query.getContract()).inspect().annotations()
						.retrive();

				// find a scope annotation
				Annotation found = null;
				for (Annotation a : annotations) {
					if (scopes.containsKey(a.annotationType().getName())) {
						if (found != null) {
							// already found one previouslly
							throw new BindingException(
									"To many auto-binding options");
						}
						found = a; // do not break
					}
				}

				// if (found!=null){
				// // found a scope
				// // add a biding
				//
				//
				// BeanModel model = this.getBeanModel(query.getContract());
				//
				// binding = new Binding();
				//
				// binding.addParams(model.getParams());
				// binding.setTargetScope(found.annotationType());
				//
				// binding.setStartType(model.getContractType());
				// binding.setResolver(FactoryResolver.instanceFor(query.getContract(),
				// binder));
				//
				//
				// this.addBinding(binding);
				//
				// return getInstance(query); // repeat search
				// } else {
				if (query.isRequired()) {
					throw new BindingNotFoundException(query.getContract());
				} else {
					return null;
				}
				// }

			}

			// binding was found
			// resolve target
			if (stack.contains(key)) {
				// cyclic reference
				// return proxy
				CyclicProxy proxy = cyclicProxies.get(key);
				if (proxy == null) {
					proxy = new CyclicProxy();
					cyclicProxies.put(key, proxy);
				}
				return Introspector.of(query.getContract()).newProxyInstance(
						proxy);

			} else {
				stack.offer(key);
				try {
					// resolve scope
					ScopePool scopePool = getScopePool(binding);

					// get resolver
					Resolver<T> resolver = binding.getResolver();

					if (resolver == null
							&& !binding.getAbstractType().isInterface()) {
						resolver = FactoryResolver.instanceFor(
								binding.getAbstractType(), this);
					}

					if (resolver != null) {
						// attach interceptors
						final InterceptorResolver<T> interceptorResolver = new InterceptorResolver<T>(
								interceptors, resolver);
						T obj = scopePool
								.getInScope(query, interceptorResolver);

						stack.remove(key);

						// check if there is any proxy to complete
						CyclicProxy proxy = cyclicProxies.get(key);
						if (proxy != null) {
							proxy.setRealObject(obj);
						}
						return obj;
					} else {
						return null;
					}
				} catch (RuntimeException e) {
					stack.remove(key);
					throw e;
				}
			}

		}

		public void wireMembers(Object obj) {
			BeanModel model = this.getBeanModel(obj.getClass());

			for (AfterWiringPoint a : model.getAfterPoints()) {
				obj = a.writeAtPoint(binder, obj);
			}
		}

		@Override
		public void removeBinding(Binding binding) {
			bindingMap.remove(binding);

		}

		public ScopePool getScopePool(Binding binding) {
			Class<? extends ScopePool> scopeClass;
			if (binding.getScope() == null) {
				scopeClass = scopes.get(Default.class.getName());
			} else {
				scopeClass = scopes.get(binding.getScope().getName());
				if (scopeClass == null) {
					throw new BindingNotFoundException(binding.getScope());
				}
			}

			// find scope class
			ScopePool scopePool = scopePools.get(scopeClass.getName());
			if (scopePool == null) {
				// the pool is also injectable
				scopePool = getInstance(WiringSpecification.search(scopeClass));
				scopePools.put(scopeClass.getName(), scopePool);

				scopePool.addScopeListener(new ScopeListener() {

					@Override
					public void onObjectAdded(ScopePoolEvent scopePoolEvent) {
						objetEvent.broadcastEvent()
								.onObjectAdded(
										new ObjectPoolEvent(scopePoolEvent
												.getObject()));
					}

					@Override
					public void onObjectRemoved(ScopePoolEvent scopePoolEvent) {
						objetEvent.broadcastEvent()
								.onObjectRemoved(
										new ObjectPoolEvent(scopePoolEvent
												.getObject()));
					}

				});
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

		/**
		 * @param beanModel
		 */
		public void addBindings(BeanModel model, Resolver<?> resolver) {

			for (Class<?> scope : model.getScopes()) {
				Binding binding = new Binding();

				binding.addParams(model.getParams());
				binding.setTargetScope(scope);

				binding.setAbstractType(model.getContractType());

				binding.setResolver(resolver);

				this.addBinding(binding);

				initializeBinding(binding);
			}

		}

	}

	private final EventListenersSet<ObjectPoolListener> objetEvent = EventListenersSet
			.newSet(ObjectPoolListener.class);

	
	protected void initializedAll(){
		for (Binding b : bindingMap.getBindings()) {
			initializeBinding(b);
		}
	}

	private void initializeBinding(Binding b) {
		if (!b.isInicialized()
				&& !b.isLazy()
				&& (Service.class.equals(b.getScope()) || Shared.class.equals(b.getScope()))) {
			try {
			this.objectPool.getInstance(b.getAbstractType());
			b.setInicialized(true);
			} catch (Exception e){
				//no-op. eager loading fail. try lazy
			}
		}
	}
	
	
	private class StandardObjectPool implements ObjectPool {

		private StandardObjectPool() {

		}

		@Override
		public ObjectPool addConfiguration(BindConfiguration... modules) {
			for (BindConfiguration m : modules) {
				m.configure(binder);
			}

			initializedAll();

			return this;
		}

		@Override
		public <T> T getInstance(Class<T> type) {
			return binder.getInstance(WiringSpecification.search(type));
		}

		@Override
		public <T> T getInstance(Class<T> type,
				Map<String, ? extends Object> params) {

			@SuppressWarnings("unchecked")
			Map<String, Object> p = (Map<String, Object>) params;

			return binder.getInstance(WiringSpecification.search(type, p));
		}

		@Override
		public void wireMembers(Object obj) {
			binder.wireMembers(obj);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addObjectCycleListener(ObjectPoolListener listener) {
			objetEvent.addListener(listener);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeObjectCycleListener(ObjectPoolListener listener) {
			objetEvent.removeListener(listener);
		}

	}

	@Override
	public void addConnector(WiringConnector... connectors) {
		for (WiringConnector c : connectors) {
			c.connect(binder);
		}
	}

}
