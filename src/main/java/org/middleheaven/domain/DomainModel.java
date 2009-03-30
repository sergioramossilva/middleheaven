package org.middleheaven.domain;

import java.util.Collection;

import org.middleheaven.domain.repository.Repository;

public interface DomainModel {
	
	public <E> Repository<E>  repositoryOf (Class<E> entityType);
	public <E,R extends Repository<E>> R repository(Class<R> repositoryType);
	public <T extends EntityModel> Collection<T> entitiesModels();	
	public EntityModel getEntityModelFor(Class<?> entityType);

	
}
