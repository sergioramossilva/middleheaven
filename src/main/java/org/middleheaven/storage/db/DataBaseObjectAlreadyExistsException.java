package org.middleheaven.storage.db;

import org.middleheaven.storage.StorageException;


public class DataBaseObjectAlreadyExistsException extends StorageException {

	public DataBaseObjectAlreadyExistsException() {
		super("Data Base Object already Exists");
	}

}
