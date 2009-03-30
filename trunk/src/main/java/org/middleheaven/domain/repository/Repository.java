package org.middleheaven.domain.repository;

import org.middleheaven.domain.DomainModel;
import org.middleheaven.storage.Query;
import org.middleheaven.util.identity.Identity;

/**
 * Logic reservatoire for object of a certain class.
 *
 * @param <E> Repository's entity
 */
public interface Repository<E> {

	public Identity getIdentityFor(E instance);
	
	public Query<E> findAll();
	
	public Query<E> findByIdentity(Identity id );
	
	public Query<E> findSame(E instance);

	public Query<E> findEquals(E instance);
	
	public E store(E entity);
	
	public void remove(E entity);
	
	public void addRepositoryListener(RepositoryListener listener);
	public void removeRepositoryListener(RepositoryListener listener);
	
	public void setDomainModel(DomainModel domainModel);

}
