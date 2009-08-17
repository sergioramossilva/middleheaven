package org.middleheaven.storage.db.datasource;

import org.middleheaven.storage.StorageException;

public class DriverNotFoundException extends StorageException {

	public DriverNotFoundException(String cause) {
		super(cause);
	}

}
