package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ProviderResolver<T> implements Resolver<T> {

	Class<Provider<T>>  providerClass;
	WiringContext injector;
	ProviderResolver(WiringContext injector , Class<Provider<T>> providerClass){
		this.providerClass = providerClass;
		this.injector = injector;
	}
	
	@Override
	public T resolve(Class<T> type, Set<Annotation> annotations) {
		return injector.getInstance(providerClass).provide();
	}



}
