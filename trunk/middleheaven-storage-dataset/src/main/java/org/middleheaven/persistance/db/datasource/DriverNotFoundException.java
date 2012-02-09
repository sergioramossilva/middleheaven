package org.middleheaven.persistance.db.datasource;

import org.middleheaven.persistance.PersistanceException;

public class DriverNotFoundException extends PersistanceException {

	public DriverNotFoundException(String cause) {
		super(cause);
	}

}
