package org.middleheaven.cache;

public abstract class AbstractCacheRegion implements CacheRegion{

	
	private String name;

	public AbstractCacheRegion (String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
