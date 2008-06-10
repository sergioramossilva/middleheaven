package org.middleheaven.storage;

public class StorageException extends RuntimeException {


	private static final long serialVersionUID = -2247431800793521934L;

	public StorageException(Throwable cause){
		super(cause);
	}
	
	public StorageException(String cause){
		super(cause);
	}
}
