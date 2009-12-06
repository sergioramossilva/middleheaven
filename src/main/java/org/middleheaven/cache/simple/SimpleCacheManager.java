package org.middleheaven.cache.simple;

import org.middleheaven.cache.CacheManager;
import org.middleheaven.cache.CacheRegion;
import org.middleheaven.cache.CacheSpecification;

public class SimpleCacheManager implements CacheManager{


	@Override
	public boolean canUse(CacheSpecification specification) {
		// TODO implement CacheManager.canUse
		return false;
	}

	@Override
	public CacheRegion createRegion(String regionName,
			CacheSpecification specification) {
		// TODO implement CacheManager.createRegion
		return null;
	}

}
