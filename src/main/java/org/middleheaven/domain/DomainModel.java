package org.middleheaven.domain;

import java.util.Collection;

import org.middleheaven.storage.StorableEntityModel;

public interface DomainModel {

	public <I extends Identity> Class<I> indentityTypeFor(Class<?> entityType);
	public <E extends Entity> Repository<E>  repositoryOf (Class<E> entityType);
	public <E extends Entity,R extends Repository<E>> R repository(Class<R> repositoryType);
	public DataStorage storageOf( Class<?> entityType);
	public  <E extends Entity>  StorableEntityModel<E> getStorableEntityModelFor(Class<?> entityType);
	public  <E extends Entity> void addEntity(Class<E> entityType, Repository<? extends E> repository);
	public Collection<StorableEntityModel> storableEntitiesModels();
		
}
