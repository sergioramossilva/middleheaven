package org.middleheaven.storage;

public class ReadStrategyRestrictionException extends StorageException {

	private static final long serialVersionUID = 9214399080343104924L;

	public ReadStrategyRestrictionException() {
		super("Read strategy limits the use of this operation");
	}

}
