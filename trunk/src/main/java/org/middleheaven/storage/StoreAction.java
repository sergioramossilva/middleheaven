/**
 * 
 */
package org.middleheaven.storage;


abstract class  StoreAction {


	private final Storable storable;

	public StoreAction (Storable storable){
		this.storable = storable;
	}

	protected Storable getStorable(){
		return storable;
	}
	
	public abstract boolean execute(DataStorage dataStorage);
}