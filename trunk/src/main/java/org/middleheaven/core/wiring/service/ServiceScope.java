package org.middleheaven.core.wiring.service;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.ScopePool;
import org.middleheaven.core.wiring.WiringSpecification;

public class ServiceScope implements ScopePool {

	Map<String, Object> proxies = new TreeMap<String,Object>(); 
	
	@Override
	public <T> T scope(WiringSpecification<T> query, Resolver<T> resolver) {

		// if there is not a proxy for this service, create one
		
		T proxy = (T)proxies.get(query.getContract().getName());
		
		if (proxy ==null){
			proxy =  query.getContract().cast(
					Proxy.newProxyInstance(
							this.getClass().getClassLoader(), 
							new Class<?>[]{query.getContract()}, 
							new ServiceProxy<T>(query.getContract())
					)
			);
			proxies.put(query.getContract().getName(),proxy);
		}
		
		return proxy;
	}




}
