package org.middleheaven.cache;

import org.middleheaven.core.annotations.Service;

@Service
public interface CacheService {

	public void register(String regionName, CacheSpecification specification);
	public void unRegister(String regionName);
	
	public Cache getCache(String region);
}
