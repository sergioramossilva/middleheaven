package org.middleheaven.storage;

public class DataStorageNotFoundException extends StorageException {

	public DataStorageNotFoundException() {
		super("Storage not found");
	}

}
