package org.middleheaven.cache;

public class LRUCacheSpecification extends CacheSpecification {

	
	public static LRUCacheSpecification getInstance(int maxObjectCount){
		LRUCacheSpecification spec =  new LRUCacheSpecification();
		spec.setMaxObjectCount(maxObjectCount);
		return spec;
	} 
	
	protected LRUCacheSpecification (){
		
	}
}
