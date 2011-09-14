package org.middleheaven.cache;

public class CacheException extends RuntimeException {

	protected CacheException(){
		super();
	}
	
	public CacheException(Exception e){
		super(e);
	}

	public CacheException(String message) {
		super(message);
	}
}
