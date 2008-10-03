package org.middleheaven.domain;


public class Domain {

	private Domain(){}
	
	protected static DomainModel model;
	
	public static <E extends Entity> StorableEntityModel<E> getEntityModel(Class<E> entityType){
		return model.getStorableEntityModelFor(entityType);
	}
	
	public static <E extends Entity> Repository<E> repositoryOf(Class<E> entityType){
		return model.repositoryOf(entityType);
	}
 
	public static <E extends Entity,R extends Repository<E>> R repository(Class<R> repositoryType){
		return model.repository(repositoryType);
	}
	
	public static <E extends Entity, R extends Repository<? super E> > R  repositoryOf(Class<E> entityType, Class<R> repositoryType){
		return repositoryType.cast(model.repositoryOf(entityType));
	}
}
