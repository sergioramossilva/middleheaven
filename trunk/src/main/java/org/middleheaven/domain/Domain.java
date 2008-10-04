package org.middleheaven.domain;

import org.middleheaven.domain.repository.Repository;


public class Domain {

	private Domain(){}
	
	protected static DomainModel model;

	public static <E> Repository<E> repositoryOf(Class<E> entityType){
		return model.repositoryOf(entityType);
	}
 
	public static <E,R extends Repository<E>> R repository(Class<R> repositoryType){
		return model.repository(repositoryType);
	}
	
	public static <E, R extends Repository<? super E> > R  repositoryOf(Class<E> entityType, Class<R> repositoryType){
		return repositoryType.cast(model.repositoryOf(entityType));
	}
}
