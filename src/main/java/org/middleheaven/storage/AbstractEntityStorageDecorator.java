package org.middleheaven.storage;

import org.middleheaven.util.criteria.entity.EntityCriteria;
import org.middleheaven.util.identity.Identity;

public abstract class AbstractEntityStorageDecorator implements EntityStore {

	/**
	 * Implement this method to retrieve the correct original EntityStore instance. 
	 * @return
	 */
	protected abstract EntityStore original();
	
	@Override
	public void addStorageListener(DataStorageListener listener) {
		this.original().addStorageListener(listener);
	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria) {
		return original().createQuery(criteria);
	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy) {
		return original().createQuery(criteria, strategy);
	}

	@Override
	public Identity getIdentityFor(Object object) {
		return this.original().getIdentityFor(object);
	}

	@Override
	public <T> void remove(T obj) {
		this.original().remove(obj);
	}

	@Override
	public <T> void remove(EntityCriteria<T> criteria) {
		this.original().remove(criteria);
	}

	@Override
	public void removeStorageListener(DataStorageListener listener) {
		this.original().removeStorageListener(listener);
	}

	@Override
	public <T> T store(T obj) {
		return original().store(obj);
	}
	
	

}
