package org.middleheaven.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.domain.repository.Repository;

public class EditableDomainModel implements DomainModel {

	Map<String, EntityModel> models = new HashMap<String,EntityModel>();
	Map<String, Repository> repositories =  new HashMap<String,Repository>();
	

	public <E> void addEntityModel(Class<E> entityType, EntityModel model) {
		models.put(entityType.getName(), model);
	}

	public <E> void addEntityRepository(Class<E> entityType, Repository<? extends E> repository) {
		repository.setDomainModel(this);
		repositories.put(entityType.getName(), repository);
	}
	
	@Override @SuppressWarnings("unchecked") // controlled by addEntity
	public <E> Repository<E> repositoryOf(Class<E> entityType) {
		return repositories.get(entityType.getName());
	}
	
	@Override
	public <T extends EntityModel> Collection<T> entitiesModels() {
		return (Collection<T>) Collections.unmodifiableCollection(models.values());
	}

	@Override
	public EntityModel getEntityModelFor(Class<?> entityType) {
		return models.get(entityType.getName());
	}

	
	@Override @SuppressWarnings("unchecked") // controlled by addEntity
	public <E, R extends Repository<E>> R repository(Class<R> repositoryType) {
		for (Repository<E> rep : repositories.values()){
			if (repositoryType.isInstance(rep)){
				return  (R) rep;
			}
		}
		throw new ModelingException(repositoryType.getName() + " not found in model");
	}





}
