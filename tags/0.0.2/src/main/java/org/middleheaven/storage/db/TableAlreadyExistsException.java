package org.middleheaven.storage.db;

import org.middleheaven.storage.StorageException;

public class TableAlreadyExistsException extends StorageException {

	public TableAlreadyExistsException() {
		super("Table already exists");
	}

}
