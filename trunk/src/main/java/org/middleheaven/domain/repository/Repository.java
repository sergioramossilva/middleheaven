package org.middleheaven.domain.repository;

import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.DataStorageService;


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
		final DataStorage defaultStorage = ServiceRegistry.getService(DataStorageService.class).getStorage();
		return new StandardEntityRepository<E>(entityType, defaultStorage);
	}

	public  <R extends EntityRepository> R getRepository(Class<R> repositoryClass){

		return (R) ReflectionUtils.newInstance(repositoryClass);

	}


	public  <E extends Entity>  void setRepository(Class<E> entityType ,  EntityRepository<E> repository){
		repositories.put(entityType.getName(), repository);
	}
}
