package org.middleheaven.storage;

import java.util.Collection;
import java.util.Set;

import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.db.StoreQuerySession;
import org.middleheaven.util.identity.Identity;

/**
 * Abstraction for a real physical data storage.
 * {@ DataStorage} object encapsulate persistence technology related translations.  
 */
public interface DataStorage {

	/**
	 * Wraps the object with a {@code Storable} interface.
	 * If the object is already a {@code Storable}, return the object as it is.
	 * @param obj
	 * @return
	 */
	public Storable merge(Object obj);
	
	/**
	 * Determine the object's identity token.
	 * @param object
	 * @return
	 */
	public Identity getIdentityFor(Object object);
	
	/**
	 * Assign identity token for the object , if it has none
	 * @return storable after assigning identity
	 */
	public Storable assignIdentity(Storable storable);
	
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

	public void flatten(Storable p, Set<Storable> all);



}
