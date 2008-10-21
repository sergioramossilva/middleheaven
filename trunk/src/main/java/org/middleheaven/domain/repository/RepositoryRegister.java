package org.middleheaven.domain.repository;

import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.reflection.ReflectionUtils;


public class RepositoryRegister {

	private  Map<String, Repository<?>> repositories = new TreeMap<String, Repository<?>>();
	
	public  <E> Repository<E> of(Class<E> entityType){
		Repository<E> rep = (Repository<E>) repositories.get(entityType.getName());

		if (rep==null){
			rep = newRepository(entityType);
			
		}
		return rep;
	}
	
	public <E> Repository<E> newRepository(Class<E> entityType){
		return new StandardEntityRepository<E>(entityType);
	}

	public  <R extends Repository> R getRepository(Class<R> repositoryClass){

		return (R) ReflectionUtils.newInstance(repositoryClass);

	}


	public  <E>  void setRepository(Class<E> entityType ,  Repository<? extends E> repository){
		repositories.put(entityType.getName(), repository);
	}
}
