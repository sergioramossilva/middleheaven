package org.middleheaven.domain.repository;

import java.util.Map;
import java.util.TreeMap;


public class Repository {

	private  Map<String, EntityRepository<?>> repositories = new TreeMap<String, EntityRepository<?>>();
	
	public  <E extends Entity> EntityRepository<E> of(Class<E> entityType){
		EntityRepository<E> rep = (EntityRepository<E>) repositories.get(entityType.getName());

		if (rep==null){
			rep = newRepository(entityType);
			
		}
		return rep;
	}
	
	public <E extends Entity> EntityRepository<E> newRepository(Class<E> entityType){
		return new StandardEntityRepository<E>(entityType,Storages.getStorage());
	}

	public  <R extends EntityRepository> R getRepository(Class<R> repositoryClass){

		return (R) ReflectionUtils.newInstance(repositoryClass);

	}


	public  <E extends Entity>  void setRepository(Class<E> entityType ,  EntityRepository<E> repository){
		repositories.put(entityType.getName(), repository);
	}
}
