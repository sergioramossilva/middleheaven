package org.middleheaven.domain;

import java.util.Collection;

import org.middleheaven.domain.repository.Repository;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.util.identity.Identity;

public interface DomainModel {

	public <I extends Identity> Class<I> indentityTypeFor(Class<?> entityType);
	public <E> Repository<E>  repositoryOf (Class<E> entityType);
	public <E,R extends Repository<E>> R repository(Class<R> repositoryType);
	public <E> DataStorage storageOf(Class<E> entityType);
	public <E> void addEntity(Class<E> entityType, Repository<? extends E> repository);
	
	public <T extends EntityModel> Collection<T> entitiesModels();	
	public EntityModel getEntityModelFor(Class<?> entityType);

	
}
