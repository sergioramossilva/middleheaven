package org.middleheaven.storage;

import java.util.Collection;

import org.middleheaven.domain.EntityModel;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.util.identity.Identity;

public interface DataStorage {

	public StorableEntityModel storableModelOf(EntityModel model);
	
	/**
	 * Wraps the object with a {@code Storable} interface.
	 * If the object is already a {@code Storable}, return the object as it is.
	 * @param obj
	 * @return
	 */
	public Storable merge(Object obj);
	
	public Identity getIdentityFor(Object object);
	
	/**
	 * Included a new objects in the store
	 * @param obj Collection of objects to include
	 */
	public void insert(Collection<Storable> obj,StorableEntityModel model);
	
	/**
	 * Updates the state of objects in the store
	 * @param obj Collection of objects to update
	 */
	public void update(Collection<Storable> obj,StorableEntityModel model);
	
	/**
	 * Removes the objects in the collection from the store
	 */
	public void remove(Collection<Storable> obj,StorableEntityModel model);
	
	/**
	 * Removes all object that match the criteria from the store
	 */
	public void remove(Criteria<?> criteria,StorableEntityModel model);
	
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
	public <T> Query<T> createQuery(Criteria<T> criteria, StorableEntityModel model, ReadStrategy strategy);
	

	/**
	 * 
	 * @return {@code true} if this keeper is responsible for assigning the storable identity, {@code false} otherwise 
	 */
	public Storable assignIdentity(Storable storable);
}
