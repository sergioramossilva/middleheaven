/**
 * 
 */
package org.middleheaven.domain.store;




/**
 * An action to be performed on a DataStorage with a given object.
 */
public abstract class StoreAction {

	private final EntityInstance object;

	public StoreAction (EntityInstance object){
		this.object = object;
	}

	protected EntityInstance getStorable(){
		return object;
	}
	
	public abstract boolean execute(EntityInstanceStorage dataStorage);
}