package org.middleheaven.storage;

import java.util.Collection;

import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.util.sequence.Sequence;

public interface StoreManager {

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
	 * Returns a sequence of longs under registered for a given name
	 * This method is intended to provide universal support for
	 * storage unique key generation in a store dependent manner.
	 * DatabaseStorages can use native sequence support where available. 
	 * @param name sequence name
	 * @return <code>Long</code> sequence
	 */
	public Sequence<Long> getSequence(String name);
}
