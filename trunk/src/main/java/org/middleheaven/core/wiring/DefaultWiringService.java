package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.core.wiring.service.ServiceScope;


public class DefaultWiringService implements WiringService{


	PropertyResolver<Object> propertyResolver = new PropertyResolver<Object>();

	DefaultWiringContext wiringContext = new DefaultWiringContext();
	BinderImpl binder = new BinderImpl();
	List<Interceptor> interceptors = new ArrayList<Interceptor>();
	Map<String,Class<? extends ScopePool>> scopes = new TreeMap<String,Class<? extends ScopePool>>();
	Map<String, ScopePool> scopePools = new TreeMap<String,ScopePool>();

	public DefaultWiringService(){
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
	}


	@Override
	public WiringContext getWiringContext() {
		return wiringContext;
	}
	
	public 

	Map<Key , Binding> bindings = new HashMap<Key , Binding>();

	private class BinderImpl implements Binder,EditableBinder,ConnectableBinder{

		Queue<Key> stack = new LinkedList<Key>();
		Map<Key, CyclicProxy> cyclicProxies = new HashMap<Key, CyclicProxy>();
		Set<WiringModelParser> parsers= new CopyOnWriteArraySet<WiringModelParser>();
		
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
		
		@Override
		public WiringModel getWiringModel(Class<?> type) {
			WiringModel model = new WiringModel();

			for (WiringModelParser parser : this.parsers){
				parser.parse(type, model);
			}
			return model;
		}
		
		@Override
		public void addWiringModelParser(WiringModelParser parser) {
			parsers.add(parser);
		}

		@Override
		public void removeWiringModelParser(WiringModelParser parser) {
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
				Annotation[] all = ReflectionUtils.getAnnotations(query.getContract());

				List <Annotation> annotations = new ArrayList<Annotation>(Arrays.asList(all));
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
					throw new BindingNotFoundException(query.getContract());
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
				return  ReflectionUtils.proxy(query.getContract(), proxy);

			} else {
				stack.offer(key);
				try{
					// resolve scope 
					ScopePool scopePool;
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
					scopePool = scopePools.get(scopeClass.getName());
					if (scopePool==null){
						scopePool = getInstance(WiringSpecification.search(scopeClass));
						scopePools.put(scopeClass.getName(), scopePool);
					}

					// get resolver
					Resolver<T> resolver = binding.getResolver();
					if (resolver==null){
						resolver =  FactoryResolver.instanceFor(binding.getAbstractType(),this);
					}
					// attach interceptors
					final InterceptorResolver<T> interceptorResolver = new InterceptorResolver<T>(interceptors,resolver);
					T obj = scopePool.scope(query, interceptorResolver);

					CyclicProxy proxy = cyclicProxies.get(key);
					if (proxy!=null){
						proxy.setRealObject(obj);
					}
					stack.remove(key);
					
					WiringModel model = this.getWiringModel(obj.getClass());
					
					for (AfterWiringPoint a : model.getAfterPoints()){
						obj = a.writeAtPoint(binder, obj);
					}
					
					return  obj;
				} catch (RuntimeException e){
					stack.remove(key);
					throw e;
				}
			}

		}

		@Override
		public void removeBinding(Binding binding) {
			bindings.remove(binding.getKey());

		}
	}

	private class DefaultWiringContext implements WiringContext{

		@Override
		public WiringContext addConfiguration(BindConfiguration... modules) {
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
			WiringModel model = binder.getWiringModel(obj.getClass());
			
			for (AfterWiringPoint a : model.getAfterPoints()){
				a.writeAtPoint(binder, obj);
			}
			
		}

	}

	@Override
	public void addConnector(WiringConnector... connectors) {
		for (WiringConnector c : connectors){
			c.connect(binder);
		}
	}
}
