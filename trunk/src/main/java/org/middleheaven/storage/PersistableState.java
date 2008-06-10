package org.middleheaven.storage;

public enum PersistableState {


	BLANK,
	FILLED,
	RETRIVED,
	EDITED,
	DELETED
	;

	public PersistableState edit(){
		if (this==BLANK){
			return FILLED;
		} else if(this==RETRIVED) {
			return EDITED;
		}
		return this;
	}

	public PersistableState delete(){
		switch (this){
		case BLANK:
		case DELETED:
			return this;
		default:
			return DELETED;
		}
	}
}
