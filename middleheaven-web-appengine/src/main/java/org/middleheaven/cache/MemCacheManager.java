/**
 * 
 */
package org.middleheaven.cache;

import org.middleheaven.cache.CacheManager;

/**
 * 
 */
public class MemCacheManager implements CacheManager{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheRegion createRegion(String regionName,
			CacheSpecification specification) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canUse(CacheSpecification specification) {
		// TODO Auto-generated method stub
		return false;
	}

}
