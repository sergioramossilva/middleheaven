package org.middleheaven.util;

import org.middleheaven.core.wiring.Provider;

public interface ProviderSource {

	
	public <T> Provider<T> providerFor(Class<T> type, String name);
}
