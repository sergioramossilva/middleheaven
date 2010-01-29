package org.middleheaven.storage;

public interface StorageUnit {

	/**
	 * Add an action to the unit
	 * @param action
	 */
	public void addAction(StoreAction action);
	
	/**
	 * Optional operation that simplifies the current actions on the ones that will really change the storage state.
	 * If, for example, an action will add a storable and anothe action will remove the same storable
	 * after simplification none of this action should be present 
	 */
	public void simplify();
	
	public void roolback();
	
	public void commitTo(DataStorage dataStorage);
}
