package org.middleheaven.persistance;


public class ReadStrategyRestrictionException extends PersistanceException {

	private static final long serialVersionUID = 9214399080343104924L;

	public ReadStrategyRestrictionException() {
		super("Read strategy limits the use of this operation");
	}

}
