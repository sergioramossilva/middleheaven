package org.middleheaven.core.wiring;


public class ProviderResolver<T> implements Resolver<T> {

	Class<Provider<T>>  providerClass;
	WiringContext injector;
	ProviderResolver(WiringContext injector , Class<Provider<T>> providerClass){
		this.providerClass = providerClass;
		this.injector = injector;
	}
	
	@Override
	public T resolve(WiringSpecification query) {
		return injector.getInstance(providerClass).provide();
	}



}
