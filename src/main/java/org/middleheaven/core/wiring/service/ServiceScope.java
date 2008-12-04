package org.middleheaven.core.wiring.service;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.ScopePool;
import org.middleheaven.core.wiring.WiringSpecification;
import org.middleheaven.util.CollectionUtils;

/**
 * Scope for services.
 * Services can be added and removed from the system, so
 * the pool in inject a proxy that can handle the service lifecycle.
 * 
 */
public class ServiceScope implements ScopePool {

	Map<ServiceKey, Object> proxies = new HashMap<ServiceKey, Object>();
	
	@Override
	public <T> T scope(WiringSpecification<T> query, Resolver<T> resolver) {

		ServiceKey key = new ServiceKey(query.getContract().getName(),query.getParams());
		T proxy = (T)proxies.get(key);
		
		if (proxy==null){
			proxy = query.getContract().cast(
					Proxy.newProxyInstance(
							this.getClass().getClassLoader(), 
							new Class<?>[]{query.getContract()}, 
							new ServiceProxy<T>(query.getContract(),query.getParams())
					)
			);
			proxies.put(key,proxy);
		}
		
		return proxy;

	}

	private static class ServiceKey {

		Map<String,String> properties;
		String contractName;

		public ServiceKey(String contractName , Map<String,String> properties) {
			if (properties==null || properties.isEmpty()){
				this.properties = Collections.emptyMap();
			} else if (!(properties instanceof SortedMap)){
				this.properties = new TreeMap<String,String>(properties);
			} else {
				this.properties = properties;
			}
			this.contractName = contractName;
		}

		public boolean equals(Object other){
			return other instanceof ServiceKey && equals((ServiceKey)other);
		}

		public int hashCode(){
			return this.contractName.hashCode();
		}

		public boolean equals(ServiceKey other){
			return this.contractName.equals(other.contractName) && CollectionUtils.equals(properties, other.properties);
		}

	}
}
