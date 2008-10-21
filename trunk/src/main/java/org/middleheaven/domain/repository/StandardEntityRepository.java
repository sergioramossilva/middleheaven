package org.middleheaven.domain.repository;

import org.middleheaven.domain.DomainModel;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.criteria.CriteriaBuilder;


public class StandardEntityRepository<E> implements Repository<E> {

	private Class<E> entityType;
	private DomainModel domainModel;
	
	public StandardEntityRepository(Class<E> entityType){
		this.entityType = entityType;
		
	}

	@Override
	public void setDomainModel(DomainModel domainModel) {
		this.domainModel = domainModel;
	}
	
	protected DataStorage getDataStorage(){
		return domainModel.storageOf(entityType);
	}
	@Override
	public void remove(E entity) {
		getDataStorage().remove(entity);
	}

	@Override
	public E store(E entity) {
		return getDataStorage().store(entity);
	}

	@Override
	public Query<E> findAll() {
		return getDataStorage().createQuery(CriteriaBuilder.search(entityType).all());
	}

	@Override
	public Query<E> findEquals(E instance) {
		return getDataStorage().createQuery(CriteriaBuilder.search(entityType).isEqual(instance).all());
	}

	@Override
	public Query<E> findSame(E instance) {
		return getDataStorage().createQuery(CriteriaBuilder.search(entityType).isSame(instance).all());
	}

	
	
}
