package org.middleheaven.cache;

public interface CacheManager {

	public CacheRegion createRegion(String regionName,CacheSpecification specification);

	public boolean canUse(CacheSpecification specification);

}
