package org.middleheaven.persistance.db.metamodel;

import org.middleheaven.persistance.PersistanceException;


public class DataBaseObjectAlreadyExistsException extends PersistanceException {


	private static final long serialVersionUID = -5524308981164671692L;

	public DataBaseObjectAlreadyExistsException() {
		super("Data Base Object already Exists");
	}

}
