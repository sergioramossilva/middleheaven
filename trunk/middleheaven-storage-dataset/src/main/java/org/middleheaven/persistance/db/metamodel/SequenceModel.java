package org.middleheaven.persistance.db.metamodel;

/**
 * Represents a model for a sequence generator.
 */
public class SequenceModel implements DataBaseObjectModel {

	String name;
	int startWith;
	int incrementBy;
	
	public SequenceModel(String name, int startWith, int incrementBy){
		this.name = name;
		this.startWith = startWith;
		this.incrementBy = incrementBy;
	}
	
	public int getStartWith() {
		return startWith;
	}

	public int getIncrementBy() {
		return incrementBy;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public DataBaseObjectType getType() {
		return DataBaseObjectType.SEQUENCE;
	}

}
