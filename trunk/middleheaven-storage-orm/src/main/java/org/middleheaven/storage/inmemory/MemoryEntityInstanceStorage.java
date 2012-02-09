/**
 * 
 */
package org.middleheaven.storage.inmemory;

import java.util.Collection;
import java.util.Collections;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.query.FixedListQuery;
import org.middleheaven.domain.query.Query;
import org.middleheaven.domain.store.DomainStoreManager;
import org.middleheaven.domain.store.EntityInstance;
import org.middleheaven.domain.store.EntityInstanceStorage;
import org.middleheaven.domain.store.ReadStrategy;

/**
 * 
 */
public class MemoryEntityInstanceStorage implements EntityInstanceStorage {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(Collection<EntityInstance> obj) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Collection<EntityInstance> obj) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(Collection<EntityInstance> obj) {
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> void remove(EntityCriteria<T> criteria) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria,
			ReadStrategy strategy) {
		return FixedListQuery.from(Collections.<T>emptyList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStorableStateManager(DomainStoreManager stateManager) {
		
	}

}
