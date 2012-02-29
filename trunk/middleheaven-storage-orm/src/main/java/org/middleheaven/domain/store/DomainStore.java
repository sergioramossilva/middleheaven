package org.middleheaven.domain.store;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.query.Query;
import org.middleheaven.util.identity.Identity;

/**
 * Standard contract for data stores.
 */
public interface DomainStore {
	
	/**
	 * Determine the identity of the object
	 * @param object
	 * @return the identity of the object or null if its has no identity
	 */
	public Identity getIdentityFor(Object object);
	
	/**
	 * Store the object.
	 * Object with the same identity are only stored once. Further storage 
	 * of the same object only updates the data for that object.
	 * @param <T> the object type.
	 * @param obj the object to store.
	 * @return
	 */
	public <T> T store(T obj);
	
	/**
	 * Remove the object from storage
	 * @param <T>
	 * @param obj
	 */
	public <T> void remove(T obj);
	
	/**
	 * Create a query from a criteria
	 * @param <T>
	 * @param criteria
	 * @return
	 */
	public <T> Query<T> createQuery(EntityCriteria<T> criteria);

	/**
	 * Create a query from a criteria informing the reading strategy that will be use
	 * for the query.
	 * @param <T>
	 * @param criteria
	 * @param strategy
	 * @return
	 */
	public <T> Query<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy);
	
	/**
	 * Remove from storage all object that match the given criteria
	 * @param <T>
	 * @param criteria
	 */
	public <T> void remove(EntityCriteria<T> criteria);
	
	/**
	 * Register a storage listener.
	 * @param listener
	 */
	public void addStorageListener(DomainStoreListener listener);
	
	/**
	 * Unregister a storage listener.
	 * @param listener
	 */
	public void removeStorageListener(DomainStoreListener listener);

	
	
}