package org.middleheaven.domain.model;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;

public class EditableDomainModel implements DomainModel {

	Map<String, EntityModel> models = new HashMap<String,EntityModel>();

	public <E> void addEntityModel(Class<E> entityType, EntityModel model) {
		models.put(entityType.getName(), model);
	}

	@Override @SuppressWarnings("unchecked") // controlled by addEntity
	public <T extends EntityModel> Enumerable<T> entitiesModels() {
		return  (Enumerable<T>) CollectionUtils.enhance(models.values());
	}

	@Override
	public EntityModel getEntityModelFor(Class<?> entityType) {
		return models.get(entityType.getName());
	}

	

}
