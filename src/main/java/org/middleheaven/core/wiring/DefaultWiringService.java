package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import org.middleheaven.core.reflection.ProxyUtils;

public class DefaultWiringService implements WiringService{
	
	@SuppressWarnings("unchecked")
	PropertyResolver propertyResolver = new PropertyResolver();
	
	DefaultWiringContext wiringContext = new DefaultWiringContext();
	BinderImpl binder = new BinderImpl();
	List<Interceptor> interceptors = new ArrayList<Interceptor>();
	Map<String,Class<? extends ScopePool>> scopes = new TreeMap<String,Class<? extends ScopePool>>();
	Map<String, ScopePool> scopePools = new TreeMap<String,ScopePool>();

	public DefaultWiringService(){
		scopes.put(Shared.class.getName(), SharedScope.class);
		scopes.put(Default.class.getName(), DefaultScope.class);
		DefaultScope defaultScope = new DefaultScope();
		SharedScope sharedScope = new SharedScope();
		
		scopePools.put(DefaultScope.class.getName(),defaultScope);
		scopePools.put(SharedScope.class.getName(),new SharedScope());
		
		binder.bind(SharedScope.class).in(Shared.class).toInstance(sharedScope);
		binder.bind(DefaultScope.class).in(Shared.class).toInstance(defaultScope);
	}
	
	
	@Override
	public WiringContext getWiringContext() {
		return wiringContext;
	}

	Map<Key , Binding> bindings = new HashMap<Key , Binding>();

	private class BinderImpl implements Binder,EditableBinder,ConnectableBinder{

		Queue stack = new LinkedList();
		Map<Key, CyclicProxy> cyclicProxies = new HashMap<Key, CyclicProxy>();

		@Override
		public <T> BindingBuilder<T> bind(Class<T> type) {
			return new BindingBuilder(this, type);
		}

		@Override
		public <S extends ScopePool> void bindScope(Class<? extends Annotation> annotation, Class<S> scopeClass) {
			scopes.put(annotation.getName(),scopeClass);
		}


		@Override
		public <T> PropertyBindingBuilder<T> bindProperty(Class<T> type) {
			Binding<T> binding = new Binding<T>();
			binding.setStartType(type);
			binding.setResolver(propertyResolver);

			PropertyBindingBuilder b = new PropertyBindingBuilder(this,binding);
			binder.addBinding(binding);

			return b;
		}

		@Override
		public void addBinding(Binding binding) {
			bindings.put(binding.getKey(), binding);
		}

		@Override
		public <T> T getInstance( WiringSpecification<T> query ){
			Key<T> key = Key.keyFor(query.getContract(), query.getSpecifications());

			Binding<T> binding = bindings.get(key);
			if (binding==null){
				// if its a concrete classe create a binding now
				if (!query.getContract().isAnnotation() && !query.getContract().isInterface()){
					this.bind(query.getContract()).to(query.getContract());
					return getInstance(query); // repeat search
				}
				throw new BindingNotFoundException(query.getContract());
			}
			if (stack.contains(key)){
				// cyclic reference
				// return proxy
				CyclicProxy proxy = cyclicProxies.get(key);
				if (proxy==null){
					proxy = new CyclicProxy();
					cyclicProxies.put(key, proxy);
				}
				return  ProxyUtils.proxy(query.getContract(), proxy);

			} else {
				stack.offer(key);
				try{
					ScopePool scopePool;
					Class<? extends ScopePool> scopeClass;
					if (binding.getScope()==null){
						scopeClass = scopes.get(Default.class.getName());
					} else {
						scopeClass = scopes.get(binding.getScope().getName());
					}
					
					scopePool = scopePools.get(scopeClass.getName());
					if (scopePool==null){
						scopePool = getInstance(WiringSpecification.search(scopeClass));
						scopePools.put(scopeClass.getName(), scopePool);
					}

					final InterceptorResolver<T> interceptorResolver = new InterceptorResolver<T>(interceptors, binding.getResolver());
					final T obj = scopePool.scope(query, interceptorResolver);

					CyclicProxy proxy = cyclicProxies.get(key);
					if (proxy!=null){
						proxy.setRealObject(obj);
					}
					stack.remove(key);
					return WireUtils.populate(binder,obj);
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


	}

	@Override
	public void addConnector(WiringConnector... connectors) {
		for (WiringConnector c : connectors){
			c.connect(binder);
		}
	}
}
