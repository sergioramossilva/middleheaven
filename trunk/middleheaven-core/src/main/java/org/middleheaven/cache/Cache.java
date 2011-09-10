package org.middleheaven.cache;


public interface Cache {

	/**
	 * Put a value-key pair on to the cache
	 * @param key
	 * @param value
	 */
	public void put(Object key, Object value);
	
	/**
	 * Return a value that matches the key
	 * @param key
	 * @return
	 */
	public Object get(Object key);
	
	/**
	 * Clear all cache content
	 */
	public void clear();
	
	/**
	 * 
	 */
	public void remove(Object key);

}
