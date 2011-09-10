package org.middleheaven.cache;

public class CacheSpecification {

	
	private int maxObjectCount;
	private boolean useMemoryCiclePurge;
	private int maxMemoryIdleTimeSeconds;
	private int purgeIntervalSeconds;
	
	/**
	 * Maximum quantity of objects in memory. If this number is surpassed 
	 * the cache will be purged. 
	 * 
	 * @return
	 */
	public int getMaxObjectCount() {
		return maxObjectCount;
	}

	public void setMaxObjectCount(int maxObjectCount) {
		this.maxObjectCount = maxObjectCount;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public boolean useMemoryCiclePurge() {
		return useMemoryCiclePurge;
	}

	public void setUseMemoryCiclePurge(boolean useMemoryCiclePurge) {
		this.useMemoryCiclePurge = useMemoryCiclePurge;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxMemoryIdleTimeSeconds() {
		return maxMemoryIdleTimeSeconds;
	}

	public void setMaxMemoryIdleTimeSeconds(int maxMemoryIdleTimeSeconds) {
		this.maxMemoryIdleTimeSeconds = maxMemoryIdleTimeSeconds;
	}

	/**
	 * 
	 * @return
	 */
	public int getPurgeIntervalSeconds() {
		return purgeIntervalSeconds;
	}

	public void setPurgeIntervalSeconds(int purgeIntervalSeconds) {
		this.purgeIntervalSeconds = purgeIntervalSeconds;
	}
	
	
	
}
