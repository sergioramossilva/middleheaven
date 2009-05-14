/**
 * 
 */
package org.middleheaven.core.services;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.core.wiring.service.ServiceProxy;
import org.middleheaven.logging.LogBook;
import org.middleheaven.util.Hash;
import org.middleheaven.util.collections.CollectionUtils;

public final class RegistryServiceContext implements ServiceContext{

	final static Set<ServiceListener> serviceListeners = new CopyOnWriteArraySet<ServiceListener>();
	final static Map<String , Set<ServiceBinding>> registry = new TreeMap<String , Set<ServiceBinding>>();
	private LogBook logBook;

	public RegistryServiceContext(LogBook book){
		this.logBook = book;
		ServiceRegistry.context = this;

		/* should bing all to shared ?
		this.addServiceListener(new ServiceListener(){

			@Override
			public void onEvent(ServiceEvent event) {
				if (event.getType().equals(ServiceEventType.ADDED) && event.serviceClass.equals(WiringService.class)){
					getService(WiringService.class, null).getObjectPool().addConfiguration(new BindConfiguration(){

						@Override
						public void configure(Binder binder) {
							binder.bind(ServiceContext.class).in(Shared.class).toInstance(RegistryServiceContext.this);
						}

					});
				}
			}

		});
		*/
	}

	@Override
	public LogBook getLogBook() {
		return logBook;
	}

	private void fireServiceTemporaryRemoved(ServiceBinding binding){
		ServiceEvent event = new ServiceEvent(
				ServiceEvent.ServiceEventType.TEMPORARY_REMOVED, 
				binding.serviceClass,
				binding.implementation,
				binding.properties
				);
		for (ServiceListener s : serviceListeners){
			s.onEvent(event);
		}
	}

	private void fireServiceAdded(ServiceBinding binding){
		ServiceEvent event = new ServiceEvent(ServiceEvent.ServiceEventType.ADDED,
				binding.serviceClass,
				binding.implementation,
				binding.properties
				);
		for (ServiceListener s : serviceListeners){
			s.onEvent(event);
		}
	}

	private void fireServiceRemoved(ServiceBinding binding){
		ServiceEvent event = new ServiceEvent(ServiceEvent.ServiceEventType.REMOVED, 		
				binding.serviceClass,
				binding.implementation,
				binding.properties
				);
		for (ServiceListener s : serviceListeners){
			s.onEvent(event);
		}
	}


	@Override
	public <T, I extends T> void register(final Class<T> serviceClass, final I implementation, Map<String,String> properties) {

		// find current binding
		ServiceBinding<T> binding = this.selectedServiceBinding(serviceClass, properties);

		// if its a perfect match ( same service class, same parameters)
		if(binding!=null && binding.match(properties) == 1){

			// perfect match found. This will be substituted
			this.fireServiceTemporaryRemoved(binding);

		} 
		
		// init binding set for the service
		ServiceBinding<T> b = new ServiceBinding<T>(properties,implementation,serviceClass);
		Set<ServiceBinding> list = registry.get(serviceClass.getName());
		if (list==null ){
			list = new CopyOnWriteArraySet<ServiceBinding>();
			registry.put(serviceClass.getName(), list);
		}

		// create and registry a proxy for the service
		proxify(b);
		
		try{
			list.add(b); // add new binding
			fireServiceAdded(b);

			// special bind 

			this.getService(WiringService.class, null).getObjectPool().addConfiguration(new BindConfiguration(){

				@Override
				public void configure(Binder binder) {
					binder.bind(serviceClass).in(Service.class).toInstance(implementation);
				}

			});

		} catch (RuntimeException e){
			list.remove(b);
			throw e;
		}

	}

	/**
	 * Unregister all implementations and options for the given service.
	 * @param serviceClass the service class to unregister.
	 */
	public void unRegister(Class<?> serviceClass) {

		Set<ServiceBinding> list = registry.get(serviceClass.getName());
		if (list!=null){
			for (ServiceBinding binding : list){
				this.fireServiceRemoved(binding);
			}
			list.clear(); // remove all for the service
			registry.remove(serviceClass.getName());
		
		}
	}

	@Override
	public <T, I extends T> void unRegister(Class<T> serviceClass,I implementation, Map<String,String> properties) {
		ServiceBinding<T> registeredBinding = selectedServiceBinding(serviceClass, properties);
		
		if (registeredBinding!=null){
			// already exists. remove it
			Set<ServiceBinding> list = registry.get(serviceClass.getName());
			if (list!=null && list.remove(registeredBinding) ){
				for (ServiceBinding binding : list){
					this.fireServiceRemoved(binding);
				}
			}
		}

	}

	/**
	 * Unregisters all services with the given implementation
	 */
	@Override
	public void unRegister(Object implementation) {
		for (Iterator<Map.Entry<String,Set<ServiceBinding>>> it= registry.entrySet().iterator(); it.hasNext();){

			Entry<String, Set<ServiceBinding>> entry = it.next();
			for (ServiceBinding binding : entry.getValue()){
				if(binding.implementation.equals(implementation)){
					it.remove();
					this.fireServiceRemoved(binding);
					// do not break. can be more than one
				}
			}
		}
	}


	private <T> ServiceBinding<T> selectedServiceBinding(Class<T> serviceClass, Map<String,String> properties){
		ServiceBinding selected = null;
		Set<ServiceBinding> list = registry.get(serviceClass.getName());
		if (list!=null && !list.isEmpty()){
			// no matching is necessary, return any
			if (properties==null || properties.isEmpty()){
				return list.iterator().next(); // random selection is ok
			}

			// rank and match (return best match)
			double rank = 0;

			for (ServiceBinding b : list){
				if (b.match(properties)>rank){
					selected = b;
				}
			}
		}
		return selected;
	}
	@Override
	public <T> T getService(Class<T> serviceClass, Map<String,String> properties) {
		ServiceBinding<T> selected = selectedServiceBinding(serviceClass, properties);

		if (selected !=null){
			return proxify(selected);
		}

		throw new ServiceNotFoundException(serviceClass.getName());
	}

	Map<ServiceKey, Object> proxies = new HashMap<ServiceKey, Object>();
	
	private <T> T proxify(ServiceBinding<T> binding){
		ServiceKey key = new ServiceKey(binding.serviceClass,binding.properties);
		T proxy = (T)proxies.get(key);
		
		if (proxy==null){
			ServiceProxy<T> handler = new ServiceProxy<T>(binding.serviceClass,binding.properties);
			
			this.addServiceListener(handler);
			
			proxy = binding.serviceClass.cast(
					Proxy.newProxyInstance(
							this.getClass().getClassLoader(),
							new Class[]{binding.serviceClass},
							handler
					)
			);
			proxies.put(key,proxy);
		}
		
		return proxy;
	}
	
	private static class ServiceKey {

		Map<String,String> properties;
		String contractName;
		private Class<?> contractClass;

		public ServiceKey(Class<?> contractClass , Map<String,String> properties) {
			if (properties==null || properties.isEmpty()){
				this.properties = Collections.emptyMap();
			} else if (!(properties instanceof SortedMap)){
				this.properties = new TreeMap<String,String>(properties);
			} else {
				this.properties = properties;
			}
			this.contractName = contractClass.getName();
			this.contractClass = contractClass;
		}

		public boolean equals(Object other){
			return other instanceof ServiceKey && equals((ServiceKey)other);
		}

		public int hashCode(){
			return this.contractName.hashCode();
		}

		public boolean equals(ServiceKey other){
			return this.contractName.equals(other.contractName) && CollectionUtils.equalContents(properties, other.properties);
		}

	}
	
	private void addServiceListener(ServiceListener s) {
		serviceListeners.add(s);
	}

	private static class ServiceBinding<C> {

		Map<String,String> properties;
		Object implementation;
		Class<C> serviceClass;

		public <I extends C> ServiceBinding(Map<String,String> properties, I implementation, Class<C> serviceClass) {
			super();
			this.serviceClass = serviceClass;
			this.implementation = implementation;

			if (properties==null || properties.isEmpty()){
				this.properties = Collections.emptyMap();
			} else {
				this.properties = new TreeMap<String,String>(properties);
			}

		}

		
		
		public boolean equals(Object other){
			return other instanceof ServiceBinding && equals((ServiceBinding)other);
		}

		public int hashCode(){
			return Hash.hash(properties).hash(serviceClass.getName()).hashCode();
		}

		public boolean equals(ServiceBinding other){
			return this.serviceClass.getName().equals(other.serviceClass.getName()) && CollectionUtils.equalContents(properties, other.properties);
		}

		/**
		 * 0 = Does not match at all
		 * 1 = Match exactly
		 * @param properties
		 * @return
		 */
		public double match (Map<String,String> properties){
			if (this.properties.size() < properties.size()){
				return 0d;
			}

			int count=0;
			for (Map.Entry<String,String> entry : properties.entrySet() ){
				if (entry.getValue()!=null && entry.getValue().equals(this.properties.get(entry.getKey()))){
					count++;
				}
			}

			return count * 1d / properties.size();
		}
	}


















}