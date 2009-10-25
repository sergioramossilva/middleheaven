/**
 * 
 */
package org.middleheaven.storage;

abstract class  StoreAction{


	protected Storable storable;
	public StoreAction (Storable storable){
		this.storable = storable;
	}

	public abstract boolean execute(DataStorage dataStorage);
}