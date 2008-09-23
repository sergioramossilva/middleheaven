package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import org.middleheaven.core.reflection.ProxyUtils;

public class DefaultWiringService implements WiringService{

	DefaultWiringContext wiringContext = new DefaultWiringContext();
	BinderImpl binder = new BinderImpl();
	List<Interceptor> interceptors = new ArrayList<Interceptor>();
	PropertyResolver propertyResolver = new PropertyResolver();
	Map<String,Class<? extends Scope>> scopes = new TreeMap<String,Class<? extends Scope>>();
	DefaultScope defaultScope = new DefaultScope();

	public DefaultWiringService(){
		scopes.put(Shared.class.getName(), SharedScope.class);
		binder.bind(SharedScope.class).toInstance(new SharedScope());
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
		public <S extends Scope> void bindScope(Class<? extends Annotation> annotation, Class<S> scopeClass) {
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
		public <T> T getInstance(Class<T> type,  Set<Annotation> specificationsSet){
			Key<T> key = Key.keyFor(type, specificationsSet);

			Binding<T> binding = bindings.get(key);
			if (binding==null){
				// if its a concrete classe create a binding now
				if (!type.isAnnotation() && !type.isInterface()){
					this.bind(type).to(type);
					return getInstance(type,specificationsSet);
				}
				throw new BindingException(type.getName() + " is not bound");
			}
			if (stack.contains(key)){
				// cyclic reference
				// return proxy
				CyclicProxy proxy = cyclicProxies.get(key);
				if (proxy==null){
					proxy = new CyclicProxy();
					cyclicProxies.put(key, proxy);
				}
				return ProxyUtils.proxy(type, proxy);

			} else {
				stack.offer(key);
				Scope scope;
				if (binding.getScope()!=null){
					Class<? extends Scope> scopeClass = scopes.get(binding.getScope().getName());
					if (scopeClass==null){
						scope = defaultScope;
					} else {
						scope = getInstance(scopeClass, new HashSet());
					}
				} else {
					scope = defaultScope;
				}

				final InterceptorResolver<T> interceptorResolver = new InterceptorResolver<T>(interceptors, binding.getResolver());
				final T obj = scope.scope(type, specificationsSet, interceptorResolver);

				CyclicProxy proxy = cyclicProxies.get(key);
				if (proxy!=null){
					proxy.setRealObject(obj);
				}
				stack.remove(key);
				return WireUtils.populate(binder,obj);
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
			return binder.getInstance(type, new HashSet<Annotation>());
		}


	}

	@Override
	public void addConnector(WiringConnector... connectors) {
		for (WiringConnector c : connectors){
			c.connect(binder);
		}
	}
}
