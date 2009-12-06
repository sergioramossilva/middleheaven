package org.middleheaven.cache.jcs;


import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.middleheaven.cache.AbstractCacheRegion;
import org.middleheaven.cache.Cache;
import org.middleheaven.cache.CacheManager;
import org.middleheaven.cache.CacheRegion;
import org.middleheaven.cache.CacheSpecification;
import org.middleheaven.cache.LRUCacheSpecification;

public class JCSCacheManager implements CacheManager {

	
	private static final JCSCacheManager manager = new JCSCacheManager();
	
	public static JCSCacheManager getManager(){
		return manager;
	}

	private JCSCacheManager(){
//		CompositeCacheManager ccm = CompositeCacheManager.getUnconfiguredInstance(); 
//
//		ccm.configure(props);
	}
	


	private static org.middleheaven.cache.CacheException handleException(CacheException e){
		return  new org.middleheaven.cache.CacheException(e);
	}
	
	private static class JCSCacheRegion extends AbstractCacheRegion implements Cache {
		JCS cache;
		
		public JCSCacheRegion(String name , JCS cache){
			super(name);
			this.cache = cache;
		}
		
		@Override
		public Cache getCache() {
			return this;
		} 
		
		@Override
		public void clear() {
			try {
				cache.clear();
			} catch (CacheException e) {
				throw handleException(e);
			}
		}

		@Override
		public Object get(Object key) {
			return cache.get(key);
		}

		@Override
		public void put(Object key, Object value) {
			try {
				cache.put(key, value);
			} catch (CacheException e) {
				throw handleException(e);
			}
		}

		@Override
		public void remove(Object key) {
			try{
				cache.remove(key);
			} catch (CacheException e) {
				throw handleException(e);
			}
		}

	
		
	}

	@Override
	public boolean canUse(CacheSpecification specification) {
		return specification instanceof LRUCacheSpecification;
	}

	@Override
	public CacheRegion createRegion(String regionName,	CacheSpecification specification) {
		try {
			// TODO add specification to properties for this region

			return new JCSCacheRegion(regionName , JCS.getInstance( regionName));
		} catch (CacheException e) {
			throw handleException(e);
		}
	}
}
