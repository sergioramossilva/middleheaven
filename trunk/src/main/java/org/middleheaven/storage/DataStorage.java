package org.middleheaven.storage;

import java.util.Collection;

import org.middleheaven.storage.criteria.Criteria;

/**
 * Abstraction for a real physical data storage.
 * {@code DataStorage} object encapsulate persistence technology related translations.  
 */
public interface DataStorage extends IdentityManager{

	
	/**
	 * Include new objects in the storage
	 * @param obj Collection of objects to include
	 */
	public void insert(Collection<Storable> obj);
	
	/**
	 * Update the state of objects in the storage
	 * @param obj Collection of objects to update
	 */
	public void update(Collection<Storable> obj);
	
	/**
	 * Removes the objects, in the collection, from the storage
	 */
	public void remove(Collection<Storable> obj);
	
	/**
	 * Removes all object, that match the criteria, from the store
	 */
	public void remove(Criteria<?> criteria);
	
	/**
	 * Creates a store-specific <code>Query</code> object using a <code>Criteria</code>
	 * The store is not immediately consult for results. The consult only occurs when one the Query 
	 * methods is invoked. Repeated invocations of the same query method may return different results as
	 * data in the store is updated
	 * @param <T> return object type
	 * @param criteria filter to be applied to the bulk data
	 * @param <code>ReadStrategy</code> read strategy
	 * @return
	 */
	public <T> Query<T> createQuery(Criteria<T> criteria, ReadStrategy strategy);

	/**
	 * Register the controling state manager
	 * @param storableStateManager
	 */
	public void setStorableStateManager(StorableStateManager storableStateManager);



}
