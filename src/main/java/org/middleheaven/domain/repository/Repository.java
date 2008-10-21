package org.middleheaven.domain.repository;

import org.middleheaven.domain.DomainModel;
import org.middleheaven.storage.Query;

/**
 * 
 * @author Sérgio Taborda
 *
 * @param <E> Repository's entity
 */
public interface Repository<E> {

	public Query<E> findAll();
	
	public Query<E> findSame(E instance);

	public Query<E> findEquals(E instance);
	
	public E store(E entity);
	
	public void remove(E entity);

	public void setDomainModel(DomainModel domainModel);
}
