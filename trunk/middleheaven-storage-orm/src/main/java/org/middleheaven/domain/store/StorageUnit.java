package org.middleheaven.domain.store;

import java.util.Collection;


/**
 * A unit of storage.
 * The interfaces established a universal contract based on the UnitOfWork pattern.
 */
public interface StorageUnit {

	/**
	 * Add an action to the unit
	 * @param action the action to be performed
	 */
	public void addAction(StoreAction action);
	
	/**
	 * Optional operation that simplifies the current actions on the ones that will really change the storage state.
	 * If, for example, an action will add a storable and anothe action will remove the same storable
	 * after simplification none of this action should be present 
	 */
	public void simplify();
	
	public void roolback();
	
	public void commitTo(EntityInstanceStorage dataStorage);

	public Collection<EntityInstance> filter(Collection<EntityInstance> all, Class<?> type);
}
