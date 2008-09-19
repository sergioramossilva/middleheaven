package org.middleheaven.domain.repository;

import org.middleheaven.storage.Query;


public interface EntityRepository<E extends Entity> {

	public Query<E> retriveAll();
	
	public Query<E> retriveSame(E instance);

	public Query<E> retriveEquals(E instance);
	
	public E save(E entity);
	
	public void delete(E entity);
}
