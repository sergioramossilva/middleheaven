package org.middleheaven.domain.repository;

import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.criteria.CriteriaBuilder;


public final class StandardEntityRepository<E extends Entity> implements EntityRepository<E> {

	private Class<E> entityType;
	private DataStorage storage;
	
	public StandardEntityRepository(Class<E> entityType , DataStorage storage){
		this.entityType = entityType;
		this.storage = storage;
	}
	
	@Override
	public void remove(E entity) {
	    storage.remove(entity);
	}

	@Override
	public E store(E entity) {
		return storage.store(entity);
	}

	@Override
	public Query<E> retriveAll() {
		return storage.createQuery(CriteriaBuilder.search(entityType).all());
	}

	@Override
	public Query<E> retriveEquals(E instance) {
		return storage.createQuery(CriteriaBuilder.search(entityType).isEqual(instance).all());
	}

	@Override
	public Query<E> retriveSame(E instance) {
		return storage.createQuery(CriteriaBuilder.search(entityType).isSame(instance).all());
	}
	
}
