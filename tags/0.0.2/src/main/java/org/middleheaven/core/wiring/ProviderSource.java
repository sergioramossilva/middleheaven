package org.middleheaven.core.wiring;


public interface ProviderSource {

	
	public <T> Provider<T> providerFor(Class<T> type, String name);
}
