package org.middleheaven.cache.simple;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.middleheaven.cache.AbstractCacheRegion;
import org.middleheaven.cache.Cache;
import org.middleheaven.cache.CacheManager;
import org.middleheaven.cache.CacheRegion;
import org.middleheaven.cache.CacheSpecification;
import org.middleheaven.cache.LRUCacheSpecification;
import org.middleheaven.cache.TimedCacheSpecification;

public class SimpleCacheManager implements CacheManager{


	@Override
	public boolean canUse(CacheSpecification specification) {
		return specification instanceof LRUCacheSpecification || specification instanceof TimedCacheSpecification;
	}

	@Override
	public CacheRegion createRegion(String regionName,	CacheSpecification specification) {
		if (specification instanceof LRUCacheSpecification){
			return new SimpleLRUCacheRegion(regionName,(LRUCacheSpecification)specification);
		} else if (specification instanceof TimedCacheSpecification){
			return new SimpleTimedCacheRegion(regionName, (TimedCacheSpecification)specification);
		}
		
		throw new org.middleheaven.cache.CacheException("Cannot use  " + specification.getClass());
	}

	private static class SimpleTimedCacheRegion extends AbstractCacheRegion implements Cache {

		private class SimpleTimedCacheInfo{

			private Object value;
			private long timestamp;
			
			public SimpleTimedCacheInfo(Object value) {
				this.value = value;
				this.timestamp = System.currentTimeMillis();
			}

			public boolean expired() {
				return  System.currentTimeMillis() - timestamp > specification.getPurgeIntervalSeconds() * 1000;
			}

			public Object getValue() {
				return value;
			}
			
		} 
		
		Map <Object,SimpleTimedCacheInfo> cache = new HashMap<Object,SimpleTimedCacheInfo>();
		private TimedCacheSpecification specification;
		
		public SimpleTimedCacheRegion(String name ,TimedCacheSpecification specification){
			super(name);
			this.specification = specification;
		}
		
		@Override
		public void clear() {
			cache.clear();
		}

		@Override
		public Object get(Object key) {
			SimpleTimedCacheInfo info = cache.get(key);
			if (info == null){
				return null;
			} else if (info.expired()){
				cache.remove(key);
				return null;
			}
			return info.getValue();
		}

		@Override
		public void put(Object key, Object value) {
			cache.put(key, new SimpleTimedCacheInfo(value));
		}

		@Override
		public void evict(Object key) {
			cache.remove(key);
		}

		@Override
		public Cache getCache() {
			return this;
		}
		

	}
	
	private static class SimpleLRUCacheRegion extends AbstractCacheRegion implements Cache {

		LinkedHashMap<Object,Object> cache = new LinkedHashMap<Object,Object>(){
			  protected boolean removeEldestEntry(Map.Entry<Object,Object> eldest) {
			        return size() > specification.getMaxObjectCount();
			  }
		};
		
		private final LRUCacheSpecification specification;
		
		public SimpleLRUCacheRegion(String name,LRUCacheSpecification specification){
			super(name);
			this.specification = specification;
		}
		
		@Override
		public Cache getCache() {
			return this;
		} 
		
		@Override
		public void clear() {
			cache.clear();
		}

		@Override
		public Object get(Object key) {
			return cache.get(key);
		}

		@Override
		public void put(Object key, Object value) {
			cache.put(key, value);
		}

		@Override
		public void evict(Object key) {
			cache.remove(key);
		}

	
	
		
	}
}
