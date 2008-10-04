package org.middleheaven.domain.repository;

import org.middleheaven.storage.Query;

/**
 * 
 * @author Sérgio Taborda
 *
 * @param <E> Repository's entity
 */
public interface Repository<E> {

	public Query<E> retriveAll();
	
	public Query<E> retriveSame(E instance);

	public Query<E> retriveEquals(E instance);
	
	public E store(E entity);
	
	public void remove(E entity);
}
