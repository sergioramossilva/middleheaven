package org.middleheaven.persistance.db.metamodel;

import org.middleheaven.persistance.db.RDBMSException;

public class TableAlreadyExistsException extends RDBMSException {

	public TableAlreadyExistsException() {
		super("Table already exists");
	}

}
