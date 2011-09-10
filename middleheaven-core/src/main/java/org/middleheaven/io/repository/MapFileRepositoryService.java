/**
 * 
 */
package org.middleheaven.io.repository;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.io.repository.engines.FileRepositoryProvider;

/**
 * 
 */
public class MapFileRepositoryService extends AbstractFileRepositoryService {

	
	private Map<String, FileRepositoryProvider> providers = new HashMap<String, FileRepositoryProvider>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerProvider(FileRepositoryProvider provider) {
		providers.put(provider.getScheme().toLowerCase(), provider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unRegisterProvider(FileRepositoryProvider provider) {
		providers.remove(provider.getScheme().toLowerCase());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FileRepositoryProvider resolveProviderByScheme(String scheme) {
		return providers.get(scheme.toLowerCase());
	}







}
