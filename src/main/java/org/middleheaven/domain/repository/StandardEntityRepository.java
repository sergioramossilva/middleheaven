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
	public void delete(E entity) {
	    Storages.getStorage().delete(entity);
	}

	@Override
	public E save(E entity) {
		return storage.save(entity);
	}

	@Override
	public Query<E> retriveAll() {
		return storage.query(CriteriaBuilder.search(entityType).all(),null);
	}

	@Override
	public Query<E> retriveEquals(E instance) {
		return storage.query(CriteriaBuilder.search(entityType).isEqual(instance).all(),null);
	}

	@Override
	public Query<E> retriveSame(E instance) {
		return storage.query(CriteriaBuilder.search(entityType).isSame(instance).all(),null);
	}
	
}
